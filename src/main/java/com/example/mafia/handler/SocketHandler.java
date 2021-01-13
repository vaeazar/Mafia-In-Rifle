package com.example.mafia.handler;

import com.example.mafia.domain.PlayerJob;
import com.example.mafia.repository.MessageMongoDBRepository;
import com.example.mafia.repository.PlayerJobMongoDBRepository;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@Component
@Slf4j
public class SocketHandler extends TextWebSocketHandler {

  @Autowired
  private MessageMongoDBRepository messageMongoDBRepository;

  @Autowired
  private PlayerJobMongoDBRepository playerJobMongoDBRepository;

  //HashMap<String, WebSocketSession> sessionMap = new HashMap<>(); //웹소켓 세션을 담아둘 맵
  private static List<HashMap<String, Object>> rls = new ArrayList<>(); //웹소켓 세션을 담아둘 리스트 ---roomListSessions

  private static List<HashMap<String, Object>> players = new ArrayList<>(); //게임중인 플레이어 정보

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    //메시지 발송
    try {
      String msg = message.getPayload();
      JSONObject obj = jsonToObjectParser(msg);

      String jsonGetType = (String) obj.get("type");
      String jsonGetRoomId = (String) obj.get("roomId");
      String jsonGetSessionId = (String) obj.get("sessionId");
      HashMap<String, Object> temp = new HashMap<>();
      if (jsonGetType.equals("jobSave")) {
        HashMap<String, String> jobSave = new HashMap<>();
        String[] jsonGetPlayerId = obj.get("playerId").toString().split("|");
        String[] jsonGetPlayerJob = obj.get("playerJob").toString().split("|");
        for (int idx = 0; idx < jsonGetPlayerId.length; idx++) {
          PlayerJob playerJob = new PlayerJob();
          playerJob.setRoomId(jsonGetRoomId);
          playerJob.setPlayerId(jsonGetPlayerId[idx]);
          playerJob.setPlayerJob(jsonGetPlayerJob[idx]);
          playerJobMongoDBRepository.insert(playerJob);
        }
      } else if (jsonGetType.equals("roomIsStart")) {
        int idx = -1;
        for (int i = 0; i < rls.size(); i++) {
          String roomId = (String) rls.get(i).get("roomId"); //세션리스트의 저장된 방번호를 가져와서
          if (roomId.equals(jsonGetRoomId)) { //같은값의 방이 존재한다면
            temp = rls.get(i); //해당 방번호의 세션리스트의 존재하는 모든 object값을 가져온다.
            idx = i;
            break;
          }
        }
        temp.put("roomStatus","start");
        if (idx != -1) {
          rls.set(idx,temp);
        }

        HashMap<String, String> jobSave = new HashMap<>();
        JSONObject startObj = new JSONObject();
        startObj.put("type", "roomIsStart");
        messageSend(jsonGetRoomId, startObj);
      } else if (jsonGetType.equals("voteStart")) {
        int idx = -1;
        for (int i = 0; i < rls.size(); i++) {
          String roomId = (String) rls.get(i).get("roomId"); //세션리스트의 저장된 방번호를 가져와서
          if (roomId.equals(jsonGetRoomId)) { //같은값의 방이 존재한다면
            temp = rls.get(i); //해당 방번호의 세션리스트의 존재하는 모든 object값을 가져온다.
            idx = i;
            break;
          }
        }
        temp.put("voteStatus","start");
        if (idx != -1) {
          rls.set(idx,temp);
        }

        JSONObject startObj = new JSONObject();
        startObj.put("type", "voteStarted");
        messageSend(jsonGetRoomId, startObj);
      } else if (rls.size() > 0) {
        String senderAlive = "";
        for (int i = 0; i < rls.size(); i++) {
          String roomId = (String) rls.get(i).get("roomId"); //세션리스트의 저장된 방번호를 가져와서
          if (roomId.equals(jsonGetRoomId)) { //같은값의 방이 존재한다면
            temp = rls.get(i); //해당 방번호의 세션리스트의 존재하는 모든 object값을 가져온다.
            senderAlive = StringUtils.isEmpty(temp.get(jsonGetSessionId + "_status")) == true ? "zombie" : temp.get(jsonGetSessionId+"_status").toString();
            break;
          }
        }

        //해당 방의 세션들만 찾아서 메시지를 발송해준다.
        for (String k : temp.keySet()) {
          Boolean doNotSend = k.equals("roomId")          //방 고유 값
                            || k.equals("adminSession")   //방장 아이디
                            || k.equals("memberList")     //맴버 정보
                            || k.equals("memberCount")    //방 인원 수
                            || k.contains("_status")      //플레이어 상태 값
                            || k.equals("voteStatus")      //플레이어 상태 값
                            || k.equals("roomStatus");    //방 상태 값

          if (doNotSend) { //방 정보 통과
            continue;
          }

          String playerStatus = StringUtils.isEmpty(temp.get(k + "_status")) == true ? "zombie" : temp.get(k+"_status").toString();

          if (senderAlive.equals("zombie") && !playerStatus.equals("zombie")) {
            continue;
          }

          WebSocketSession wss = (WebSocketSession) temp.get(k);
          if (wss != null) {
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
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    //소켓 연결
    super.afterConnectionEstablished(session);
    //sessionMap.put(session.getId(), session);
    boolean flag = false;
    JSONObject obj = new JSONObject();
    JSONObject listObj = new JSONObject();
    String url = session.getUri().toString();
    System.out.println(url);
    String tempUrlString[] = url.split("/chating/")[1].split("_");
    String roomId = "";
    String userName = "";
    if (tempUrlString.length > 1) {
      roomId = url.split("/chating/")[1].split("_")[0];
      userName = URLDecoder.decode(url.split("/chating/")[1].split("_")[1],"UTF-8");
    } else {
      roomId = "";
      userName = "";
    }
    int idx = rls.size(); //방의 사이즈를 조사한다.
    if (rls.size() > 0) {
      for (int i = 0; i < rls.size(); i++) {
        String getRoomId = (String) rls.get(i).get("roomId");
        if (getRoomId.equals(roomId)) {
          flag = true;
          idx = i;
          break;
        }
      }
    }

    if (flag) { //존재하는 방이라면 세션만 추가한다.
      JSONObject failObj = new JSONObject();
      HashMap<String, Object> map = rls.get(idx);
      HashMap<String, String> memberList = (HashMap<String, String>) rls.get(idx).get("memberList");
      String roomStatus = ObjectUtils.isEmpty(map.get("roomStatus")) == true ? "start" : map.get("roomStatus").toString();
      int memberCount = (int) map.get("memberCount");
      if (memberCount > 14) {
        failObj.put("type", "fail");
        failObj.put("failReason", "fullBang");
        failObj.put("failMessage", "최대 인원이라 참가가 불가능합니다.");
        session.sendMessage(new TextMessage(failObj.toJSONString()));
        return;
      } else if (roomStatus.equals("start")) {
        failObj.put("type", "fail");
        failObj.put("failReason", "joinFailed");
        failObj.put("failMessage", "이미 시작 된 방입니다.");
        session.sendMessage(new TextMessage(failObj.toJSONString()));
        return;
      } else if (memberList.size() > 0 && memberList.get(userName) != null) {
        failObj.put("type", "fail");
        failObj.put("failReason", "nameExist");
        failObj.put("failMessage", "중복 된 이름이 있습니다.");
        session.sendMessage(new TextMessage(failObj.toJSONString()));
        return;
      } else if (memberCount == 0) {
        map.put("adminSession", session.getId());
        obj.put("isAdmin", true);
      }
      listObj.put("type", "memberList");
      listObj.put("newMemberName", userName);
      listObj.put("failMessage", "존재하지 않는 방입니다.");
      memberList.put(userName, session.getId());
      List<String> memberNames = new ArrayList<>(memberList.keySet());
      listObj.put("memberList", memberNames);
      map.put(session.getId(), session);
      map.put(session.getId() + "_status", "alive");
      map.put("memberList", memberList);
      map.put("memberCount", ++memberCount);
    } else {
      JSONObject failObj = new JSONObject();
      failObj.put("type", "fail");
      failObj.put("failReason", "deletedRoom");
      failObj.put("failMessage", "존재하지 않는 방입니다.");
      session.sendMessage(new TextMessage(failObj.toJSONString()));
      return;
//      HashMap<String, Object> map = new HashMap<>();
//      HashMap<String, String> memberList = new HashMap<>();
//      memberList.put(userName,session.getId());
//      map.put("roomId", roomId);
//      map.put(session.getId(), session);
//      map.put("memberList", memberList);
//      map.put("adminSession", session.getId());
//      map.put("memberCount", 1);
//      obj.put("isAdmin", true);
//      rls.add(map);
    }

    //세션등록이 끝나면 발급받은 세션ID값의 메시지를 발송한다.
    obj.put("type", "getId");
    obj.put("sessionId", session.getId());
    session.sendMessage(new TextMessage(obj.toJSONString()));
    messageSend(roomId,listObj);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    //소켓 종료
    //sessionMap.remove(session.getId());
    JSONObject obj = new JSONObject();
    //소켓 종료
    if (rls.size() > 0) { //소켓이 종료되면 해당 세션값들을 찾아서 지운다.
      for (int i = 0; i < rls.size(); i++) {
        if (rls.get(i).get(session.getId()) != null) {
          HashMap<String, Object> map = rls.get(i);
          int memberCount = (int) map.get("memberCount");
          HashMap<String, String> memberList = (HashMap<String, String>) map.get("memberList");
          Boolean isAdmin = checkAdmin((String) map.get("adminSession"), session.getId());
          memberList.remove(getKey(memberList, session.getId()));
          map.put("memberCount", --memberCount);
          map.put("memberList", memberList);
          Object tempKey = getFirstKey(memberList);
          String roomStatus = ObjectUtils.isEmpty(map.get("roomStatus")) == true ? "wait" : map.get("roomStatus").toString();
          if (tempKey != null) {
            if (isAdmin && roomStatus.equals("wait")) {
              String sessionKey = memberList.get(tempKey);
              WebSocketSession wss = (WebSocketSession) map.get(sessionKey);
              obj.put("type", "adminLeft");
              obj.put("isAdmin", true);
              wss.sendMessage(new TextMessage(obj.toJSONString()));
              map.put("adminSession", sessionKey);
            }
            map.remove(session.getId());
            rls.set(i, map);
          } else {
            if (!StringUtils.isEmpty(rls.get(i).get(session.getId()))) {
              URL url = new URL("http://localhost:8080/roomDelete/" + rls.get(i).get("roomId"));
              HttpURLConnection con = (HttpURLConnection) url.openConnection();
              con.setRequestMethod("POST");
              int responseCode = con.getResponseCode();
              if (responseCode == 200) {
                log.info("roomDelete complete!! roomId : {}", rls.get(i).get("roomId"));
              } else {
                log.info("roomDelete fail!! roomId : {}", rls.get(i).get("roomId"));
              }
              con.disconnect();
              rls.remove(i);
            }
          }
        }
      }
    }
    super.afterConnectionClosed(session, status);
  }

  public int getRoomCount(String roomId) {
    if (rls.size() > 0) {
      for (int i = 0; i < rls.size(); i++) {
        String getRoomId = (String) rls.get(i).get("roomId");
        if (getRoomId.equals(roomId)) {
          return (int) rls.get(i).get("memberCount");
        }
      }
    }
    return 0;
  }

  public HashMap<String, String> getMemberList(String roomId) {
    if (rls.size() > 0) {
      for (int i = 0; i < rls.size(); i++) {
        String getRoomId = (String) rls.get(i).get("roomId");
        if (getRoomId.equals(roomId)) {
          return (HashMap<String, String>) rls.get(i).get("memberList");
        }
      }
    }
    return new HashMap<>();
  }

  public List<String> getMemberNames(String roomId) {
    if (rls.size() > 0) {
      for (int i = 0; i < rls.size(); i++) {
        String getRoomId = (String) rls.get(i).get("roomId");
        if (getRoomId.equals(roomId)) {
          List<String> memberNameList = new ArrayList<>();
          HashMap<String, String> memberList = (HashMap<String, String>) rls.get(i).get("memberList");
          for (String memberName : memberList.keySet()) {
            memberNameList.add(memberName);
          }
          return memberNameList;
        }
      }
    }
    return new ArrayList<>();
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

  public static Object getKey(HashMap<String, String> getHashMap, Object value) {
    for (Object o : getHashMap.keySet()) {
      if (getHashMap.get(o).equals(value)) {
        return o;
      }
    }
    return null;
  }

  public static Boolean checkAdmin(String adminId, String nowId) {
    return adminId.equals(nowId);
  }


  public static Object getFirstKey(HashMap<String, String> getHashMap) {
    for (Object o : getHashMap.keySet()) {
      if (getHashMap.get(o) != null) {
        return o;
      }
    }
    return null;
  }

  public static void setRoomId(String roomId) { //최초 생성하는 방이라면 방번호와 세션을 추가한다.
    HashMap<String, Object> map = new HashMap<>();
    map.put("roomId", roomId);
    map.put("memberList", new HashMap<>());
    map.put("memberCount", 0);
    map.put("roomStatus", "wait");
    rls.add(map);
  }

  private void messageSend(String jsonGetRoomId, JSONObject sendObj) {
    try {
      HashMap<String, Object> temp = new HashMap<>();

      for (int i = 0; i < rls.size(); i++) {
        String roomId = (String) rls.get(i).get("roomId"); //세션리스트의 저장된 방번호를 가져와서
        if (roomId.equals(jsonGetRoomId)) { //같은값의 방이 존재한다면
          temp = rls.get(i); //해당 방번호의 세션리스트의 존재하는 모든 object값을 가져온다.
          break;
        }
      }

      //해당 방의 세션들만 찾아서 메시지를 발송해준다.
      for (String k : temp.keySet()) {
        Boolean doNotSend = k.equals("roomId")          //방 고유 값
                          || k.equals("adminSession")   //방장 아이디
                          || k.equals("memberList")     //맴버 정보
                          || k.equals("memberCount")    //방 인원 수
                          || k.contains("_status")      //플레이어 상태 값
                          || k.equals("voteStatus")      //플레이어 상태 값
                          || k.equals("roomStatus");    //방 상태 값

        if (doNotSend) { //방 정보 통과
          continue;
        }

        WebSocketSession wss = (WebSocketSession) temp.get(k);
        if (wss != null) {
          wss.sendMessage(new TextMessage(sendObj.toJSONString()));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<Map<String, String>> giveJobs(String roomId) {
    Map<String, String> jobs = new HashMap<>();
    int idx = 0;
    for (int i = 0; i < rls.size(); i++) {
      String getRoomId = (String) rls.get(i).get("roomId");
      if (getRoomId.equals(roomId)) {
        idx = i;
        break;
      }
    }
    Map<String, String> memberList = (Map<String, String>) rls.get(idx).get("memberList");
    int memberCnt = memberList.size();
    String[] specialJobs = new String[]{"mafia","mafia","cop","doctor"};
    ArrayList<Map<String, String>> jobList = new ArrayList<>();
    ArrayList<String> memberNameList = new ArrayList<>();

    for (String memberName : memberList.keySet()) {
      memberNameList.add(memberName);
    }
    Collections.shuffle(memberNameList);

    for (int i=0;i<4;i++) {
      Map<String, String> map = new HashMap<>();
      map.put("name",memberNameList.get(i));
      map.put("job",specialJobs[i]);
      jobList.add(map);
    }

    for (int i=4;i<memberCnt;i++) {
      Map<String, String> map = new HashMap<>();
      map.put("name",memberNameList.get(i));
      map.put("job","citizen");
      jobList.add(map);
    }

    return jobList;
  }

  public void cutOffHerHead(String jsonGetRoomId, String userName, Boolean resultEqual) {
    try {
      HashMap<String, Object> temp = new HashMap<>();
      Boolean excecutedFlag = resultEqual;

      int idx = -1;
      for (int i = 0; i < rls.size(); i++) {
        String roomId = (String) rls.get(i).get("roomId"); //세션리스트의 저장된 방번호를 가져와서
        if (roomId.equals(jsonGetRoomId)) { //같은값의 방이 존재한다면
          temp = rls.get(i); //해당 방번호의 세션리스트의 존재하는 모든 object값을 가져온다.
          idx = i;
          break;
        }
      }

      if (idx != -1) {
        HashMap<String, String> memberList = (HashMap<String, String>) temp.get("memberList");
        if (StringUtils.isEmpty(memberList.get(userName))) {
          excecutedFlag = true;
        } else {
          String excecutedPlayer = memberList.get(userName) + "_status";
          temp.put(excecutedPlayer,"zombie");
          rls.set(idx,temp);
        }
      }

      if (excecutedFlag) {
        JSONObject sendObj = new JSONObject();
        sendObj.put("type", "resultEqual");
        messageSend(jsonGetRoomId, sendObj);
      } else {
        JSONObject sendObj = new JSONObject();
        sendObj.put("type", "excecuteComplete");
        sendObj.put("memberName", userName);
        messageSend(jsonGetRoomId, sendObj);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}