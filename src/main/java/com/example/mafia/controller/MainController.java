package com.example.mafia.controller;

import com.example.mafia.domain.Room;
import com.example.mafia.handler.SocketHandler;
import java.net.Socket;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Controller
public class MainController {

  List<Room> roomList = new ArrayList<Room>();
  static int roomNumber = 0;

  @Autowired
  SocketHandler socketHandler;


  @RequestMapping("/")
  public ModelAndView Index() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("room");
    return mv;
  }

  /**
   * 방 페이지
   * @return
   */
  @RequestMapping("/room")
  public ModelAndView room() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("room");
    return mv;
  }

//  /**
//   * 방 생성하기
//   * @param params
//   * @return
//   */
//  @RequestMapping("/createRoom")
//  public @ResponseBody
//  List<Room> createRoom(@RequestParam HashMap<Object, Object> params){
//    String roomName = (String) params.get("roomName");
//    if(roomName != null && !roomName.trim().equals("")) {
//      Room room = new Room();
//      room.setRoomNumber(++roomNumber);
//      room.setRoomName(roomName);
//      room.setRoomCount(0);
//      room.setRoomStatus(true);
//      String uniqueValue = UUID.randomUUID().toString().substring(0,8);
//      uniqueValue += System.currentTimeMillis();
//      room.setRoomId(uniqueValue);
//      SocketHandler.setRoomId(uniqueValue);
//      roomList.add(room);
//    }
//    return roomList;
//  }

  /**
   * 방 생성하기
   * @param params
   * @return
   */
  @RequestMapping("/createRoom")
  public ModelAndView createRoom(@RequestParam HashMap<Object, Object> params, HttpServletRequest request) {
    ModelAndView mv = new ModelAndView();
    String roomName = (String) params.get("roomName");
    System.out.println("roomName = " + roomName);
    String url = request.getHeader("referer");
    System.out.println("refffffffff = " + request.getHeader("referer"));
    if (url == null) {
      mv.addObject("errorFlag","deletedRoom");
      mv.addObject("errorMessage", "비정상적인 접근입니다.");
      mv.setViewName("room");
      return mv;
    } else if (ObjectUtils.isEmpty(params.get("userId"))) {
      mv.addObject("errorFlag","deletedRoom");
      mv.addObject("errorMessage", "아이디를 입력해주세요.");
      mv.setViewName("room");
      return mv;
    }
    if(roomName != null && !roomName.trim().equals("")) {
      Room room = new Room();
      room.setRoomNumber(++roomNumber);
      room.setRoomName(roomName);
      room.setRoomCount(0);
      room.setRoomStatus(true);
      String uniqueValue = UUID.randomUUID().toString().substring(0,8);
      uniqueValue += System.currentTimeMillis();
      room.setRoomId(uniqueValue);
      SocketHandler.setRoomId(uniqueValue);
      roomList.add(room);
      mv.addObject("roomName", params.get("roomName"));
      mv.addObject("roomNumber", roomNumber);
      mv.addObject("roomId", uniqueValue);
      mv.addObject("userId", params.get("userId"));
      mv.setViewName("mafiaChat");
      System.out.println("roomList = " + roomList.size());
    } else {
      mv.addObject("errorFlag","deletedRoom");
      mv.addObject("errorMessage", "존재하지 않는 방입니다.");
      mv.setViewName("room");
    }
    return mv;
  }

  /**
   * 방 정보가져오기
   * @param params
   * @return
   */
  @RequestMapping("/getRoom")
  public @ResponseBody List<Room> getRoom(@RequestParam HashMap<Object, Object> params){
    List <Room> sendList = new ArrayList<>();
    for (int i = 0; i < roomList.size(); i++) {
      Room room = roomList.get(i);
      room.setRoomCount(socketHandler.getRoomCount(room.getRoomId()));
      roomList.set(i,room);
      if (socketHandler.getRoomCount(room.getRoomId()) > 0) {
        sendList.add(room);
      }
    }
    return sendList;
  }

  /**
   * 방 시작
   * @param params
   * @return
   */
  @RequestMapping("/setRoomStart")
  public @ResponseBody void setRoomStart(@RequestParam HashMap<Object, Object> params){
    for (int i = 0; i < roomList.size(); i++) {
      Room room = roomList.get(i);
      if (room.getRoomId().equals(params.get("roomId"))) {
        room.setRoomStatus(false);
        roomList.set(i,room);
      }
    }
  }

  /**
   * 방 제거
   * @param params
   * @return
   */
  @RequestMapping("/delRoom")
  public @ResponseBody void delRoom(@RequestParam HashMap<Object, Object> params){
    for (int i = 0; i < roomList.size(); i++) {
      Room room = roomList.get(i);
      if (room.getRoomId().equals(params.get("roomId"))) {
        room.setRoomStatus(false);
        roomList.remove(i);
      }
    }
  }

  /**
   * 채팅방
   * @return
   */
  @RequestMapping("/moveChating")
  public ModelAndView chating(@RequestParam HashMap<Object, Object> params, HttpServletRequest request) {
    ModelAndView mv = new ModelAndView();
    String url = request.getHeader("referer");
    if (url == null) {
      mv.addObject("errorFlag","deletedRoom");
      mv.addObject("errorMessage", "비정상적인 접근입니다.");
      mv.setViewName("room");
      return mv;
    } else if (ObjectUtils.isEmpty(params.get("userId"))) {
      mv.addObject("errorFlag","deletedRoom");
      mv.addObject("errorMessage", "아이디를 입력해주세요.");
      mv.setViewName("room");
      return mv;
    }
    int roomNumber = Integer.parseInt((String) params.get("roomNumber"));
    String roomId = params.get("roomId").toString();

    List<Room> new_list = roomList.stream().filter(o->o.getRoomNumber()==roomNumber).collect(Collectors.toList());
    if(new_list != null && new_list.size() > 0) {
      mv.addObject("roomName", params.get("roomName"));
      mv.addObject("roomNumber", params.get("roomNumber"));
      mv.addObject("roomId", params.get("roomId"));
      mv.addObject("userId", params.get("userId"));
      mv.setViewName("mafiaChat");
    }else {
      mv.addObject("errorFlag","deletedRoom");
      mv.addObject("errorMessage", "존재하지 않는 방입니다.");
      mv.setViewName("room");
    }
    return mv;
  }

  @RequestMapping("/roomDelete/{id}")
  public @ResponseBody
  String roomDelete(@PathVariable("id") String roomId) {
    try {
      for(int i=0; i<roomList.size(); i++) {
        String getRoomId = roomList.get(i).getRoomId();
        if (!StringUtils.isEmpty(getRoomId) && getRoomId.equals(roomId)) {
          roomList.remove(i);
          break;
        }
      }
      return "deleteComplete";
    } catch (Exception e) {
      return "deleteFail";
    }
  }
}
