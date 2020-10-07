package com.example.mafia.handler;

import com.example.mafia.domain.MafiaMessage;
import com.example.mafia.repository.MessageMongoDBRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SocketHandler extends TextWebSocketHandler {

  @Autowired
  private MessageMongoDBRepository messageMongoDBRepository;

  //HashMap<String, WebSocketSession> sessionMap = new HashMap<>(); //웹소켓 세션을 담아둘 맵
  List<HashMap<String, Object>> rls = new ArrayList<>(); //웹소켓 세션을 담아둘 리스트 ---roomListSessions

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    //메시지 발송
    try {
      String msg = message.getPayload();
      JSONObject obj = jsonToObjectParser(msg);

      String jsonGetRoomId = (String) obj.get("roomId");
      HashMap<String, Object> temp = new HashMap<String, Object>();
      if(rls.size() > 0) {
        for(int i=0; i<rls.size(); i++) {
          String roomId = (String) rls.get(i).get("roomId"); //세션리스트의 저장된 방번호를 가져와서
          if(roomId.equals(jsonGetRoomId)) { //같은값의 방이 존재한다면
            temp = rls.get(i); //해당 방번호의 세션리스트의 존재하는 모든 object값을 가져온다.
            break;
          }
        }

        //해당 방의 세션들만 찾아서 메시지를 발송해준다.
        for(String k : temp.keySet()) {
          if(k.equals("roomId")) { //다만 방번호일 경우에는 건너뛴다.
            continue;
          }

          if(k.equals("adminSession") || k.equals("memberList") || k.equals("memberCount")) { //다만 방번호일 경우에는 건너뛴다.
            continue;
          }

          WebSocketSession wss = (WebSocketSession) temp.get(k);
          if(wss != null) {
            wss.sendMessage(new TextMessage(obj.toJSONString()));
          }
        }
      }
//      MafiaMessage sendData = new MafiaMessage();
//      sendData.setUserId(obj.get("userName").toString());
//      sendData.setMessage(obj.get("msg").toString());
//      messageMongoDBRepository.insert(sendData);
//      for (String key : sessionMap.keySet()) {
//        WebSocketSession wss = sessionMap.get(key);
//        wss.sendMessage(new TextMessage(obj.toJSONString()));
//      }
    }catch(IOException e) {
      e.printStackTrace();
    }catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    //소켓 연결
    super.afterConnectionEstablished(session);
    //sessionMap.put(session.getId(), session);
    boolean flag = false;
    String url = session.getUri().toString();
    System.out.println(url);
    String roomId = url.split("/chating/")[1].split("_")[0];
    String userName = url.split("/chating/")[1].split("_")[1];
    int idx = rls.size(); //방의 사이즈를 조사한다.
    if(rls.size() > 0) {
      for(int i=0; i<rls.size(); i++) {
        String getRoomId = (String) rls.get(i).get("roomId");
        if(getRoomId.equals(roomId)) {
          flag = true;
          idx = i;
          break;
        }
      }
    }

    if(flag) { //존재하는 방이라면 세션만 추가한다.
      JSONObject failObj = new JSONObject();
      HashMap<String, Object> map = rls.get(idx);
      List<String> memberList = (List<String>) map.get("memberList");
      List<String> result = memberList
        .stream()
        .filter(x -> x.contains(userName))
        .collect(Collectors.toList());
      int memberCount = (int) map.get("memberCount");
      if (memberCount > 14) {
        failObj.put("type", "fail");
        failObj.put("failReason", "최대 인원이라 참가가 불가능합니다.");
        session.sendMessage(new TextMessage(failObj.toJSONString()));
        return;
      } else if (!result.isEmpty()) {
        failObj.put("type", "fail");
        failObj.put("failReason", "중복 된 이름이 있습니다.");
        session.sendMessage(new TextMessage(failObj.toJSONString()));
        return;
      } else if (memberCount == 0) {
        map.put("adminSession",session.getId());
      }
      memberList.add(userName);
      map.put(session.getId(), session);
      map.put("memberList", memberList);
      map.put("memberCount", ++memberCount);
    }else { //최초 생성하는 방이라면 방번호와 세션을 추가한다.
      HashMap<String, Object> map = new HashMap<String, Object>();
      List<String> memberList = new ArrayList<>();
      memberList.add(userName);
      map.put("roomId", roomId);
      map.put(session.getId(), session);
      map.put("memberList", memberList);
      map.put("adminSession", session.getId());
      map.put("memberCount", 1);
      rls.add(map);
    }

    //세션등록이 끝나면 발급받은 세션ID값의 메시지를 발송한다.
    JSONObject obj = new JSONObject();
    obj.put("type", "getId");
    obj.put("sessionId", session.getId());
    session.sendMessage(new TextMessage(obj.toJSONString()));
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    //소켓 종료
    //sessionMap.remove(session.getId());
    //소켓 종료
    if(rls.size() > 0) { //소켓이 종료되면 해당 세션값들을 찾아서 지운다.
      for(int i=0; i<rls.size(); i++) {
        if (rls.get(i).get(session.getId()) != null) {
          System.out.println("세션 제거");
          HashMap<String, Object> map = rls.get(i);
          int memberCount = (int) map.get("memberCount");
          map.put("memberCount", --memberCount);
        }
        //System.out.println("rls.get(i).get(session.getId()) = " + rls.get(i).get(session.getId()));
        rls.get(i).remove(session.getId());
      }
    }
    super.afterConnectionClosed(session, status);
  }

  public int getRoomCount(String roomId) {
    if(rls.size() > 0) {
      for(int i=0; i<rls.size(); i++) {
        String getRoomId = (String) rls.get(i).get("roomId");
        if(getRoomId.equals(roomId)) {
          return (int) rls.get(i).get("memberCount");
        }
      }
    }
    return 0;
  }

  private static JSONObject jsonToObjectParser(String jsonStr) {
    JSONParser parser = new JSONParser();
    JSONObject obj = null;
    try {
      obj = (JSONObject) parser.parse(jsonStr);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return obj;
  }
}
