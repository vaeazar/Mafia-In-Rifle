package com.example.mafia.controller;

import com.example.mafia.domain.Room;
import com.example.mafia.handler.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

  /**
   * 방 생성하기
   * @param params
   * @return
   */
  @RequestMapping("/createRoom")
  public @ResponseBody
  List<Room> createRoom(@RequestParam HashMap<Object, Object> params){
    String roomName = (String) params.get("roomName");
    if(roomName != null && !roomName.trim().equals("")) {
      Room room = new Room();
      room.setRoomNumber(++roomNumber);
      room.setRoomName(roomName);
      room.setRoomCount(0);
      room.setRoomStatus(true);
      String uniqueValue = UUID.randomUUID().toString().substring(0,8);
      uniqueValue += System.currentTimeMillis();
      room.setRoomId(uniqueValue);
      roomList.add(room);
    }
    return roomList;
  }

  /**
   * 방 정보가져오기
   * @param params
   * @return
   */
  @RequestMapping("/getRoom")
  public @ResponseBody List<Room> getRoom(@RequestParam HashMap<Object, Object> params){
    for (int i = 0; i < roomList.size(); i++) {
      Room room = roomList.get(i);
      room.setRoomCount(socketHandler.getRoomCount(room.getRoomId()));
      roomList.set(i,room);
    }
    return roomList;
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
   * 채팅방
   * @return
   */
  @RequestMapping("/moveChating")
  public ModelAndView chating(@RequestParam HashMap<Object, Object> params, HttpServletRequest request) {
    ModelAndView mv = new ModelAndView();
    String url = request.getHeader("referer");
    if (url == null) {
      mv.addObject("alertKind", "wrongPath");
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
      mv.setViewName("mafiaChat");
    }else {
      mv.setViewName("room");
    }
    return mv;
  }
}
