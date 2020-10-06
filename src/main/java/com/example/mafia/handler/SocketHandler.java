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

import java.util.HashMap;

@Component
public class SocketHandler extends TextWebSocketHandler {

  @Autowired
  private MessageMongoDBRepository messageMongoDBRepository;

  HashMap<String, WebSocketSession> sessionMap = new HashMap<>(); //웹소켓 세션을 담아둘 맵

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    //메시지 발송
    try {
      String msg = message.getPayload();
      JSONObject obj = jsonToObjectParser(msg);
      MafiaMessage sendData = new MafiaMessage();
      sendData.setUserId(obj.get("userName").toString());
      sendData.setMessage(obj.get("msg").toString());
      messageMongoDBRepository.insert(sendData);
      for (String key : sessionMap.keySet()) {
        WebSocketSession wss = sessionMap.get(key);
        wss.sendMessage(new TextMessage(obj.toJSONString()));
      }
    }catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    //소켓 연결
    super.afterConnectionEstablished(session);
    sessionMap.put(session.getId(), session);
    JSONObject obj = new JSONObject();
    obj.put("type", "getId");
    obj.put("sessionId", session.getId());
    session.sendMessage(new TextMessage(obj.toJSONString()));
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    //소켓 종료
    sessionMap.remove(session.getId());
    super.afterConnectionClosed(session, status);
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
