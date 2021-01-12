package com.example.mafia.controller;

import com.example.mafia.domain.Room;
import com.example.mafia.domain.Vote;
import com.example.mafia.handler.SocketHandler;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
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

  @RequestMapping("/startGame/{id}")
  public @ResponseBody
  ArrayList<Map<String, String>> startGame(@PathVariable("id") String roomId) {
    ArrayList<Map<String, String>> jobs = new ArrayList<>();
    for (int i=0;i<roomList.size();i++) {
      String getRoomId = roomList.get(i).getRoomId();
      if (!StringUtils.isEmpty(getRoomId) && getRoomId.equals(roomId)) {
        jobs = socketHandler.giveJobs(roomId);
      }
    }
    return jobs;
  }

  @RequestMapping("/getMemberNames")
  public @ResponseBody
  String getMemberNames(@RequestParam HashMap<Object, Object> params) {
    String roomId = StringUtils.isEmpty(params.get("roomId")) == true ? "" : params.get("roomId").toString();
    JSONObject memberListString = new JSONObject();

    if (roomId.equals("")) {
      return memberListString.toJSONString();
    }
    List<String> memberList = socketHandler.getMemberNames(roomId);
    try {
      if (memberList == null) memberList = new ArrayList<>();
      memberListString.put("memberList",memberList);
      return memberListString.toJSONString();
    } catch (Exception e) {
      memberListString.put("memberList",new ArrayList<>());
      return memberListString.toJSONString();
    }
  }

  @RequestMapping("/BBalGangEDa/{roomId}/{voteId}")
  public @ResponseBody
  String BBalGangEDa(@PathVariable("roomId") String roomId,@PathVariable("voteId") String playerId) {
    try {
      for(int i=0; i<roomList.size(); i++) {
        String getRoomId = roomList.get(i).getRoomId();
        if (!StringUtils.isEmpty(getRoomId) && getRoomId.equals(roomId)) {
          HashMap<String, Integer> votes = roomList.get(i).getVotes();
          if (CollectionUtils.isEmpty(votes)) {
            votes.put(playerId,1);
          } else {
            votes.put(playerId, votes.get(playerId) + 1);
          }
          roomList.get(i).setVotes(votes);
          break;
        }
      }
      return "voteComplete";
    } catch (Exception e) {
      return "voteFail";
    }
  }

  @RequestMapping("/cutOffHerHead")
  public @ResponseBody
  String cutOffHerHead(@PathVariable("roomId") String roomId) {
    try {
      int userCount = 0;
      String userName = "";
      Boolean resultEqual = false;
      for(int i=0; i<roomList.size(); i++) {
        String getRoomId = roomList.get(i).getRoomId();
        if (!StringUtils.isEmpty(getRoomId) && getRoomId.equals(roomId)) {
          HashMap<String, Integer> votes = roomList.get(i).getVotes();
          votes.forEach((key, value) -> {
          });
          Iterator<String> keys = votes.keySet().iterator();
          while( keys.hasNext() ){
            String key = keys.next();
            if (userCount < votes.get(key)) {
              userCount = votes.get(key);
              userName = key;
              resultEqual = false;
            } else if (userCount == votes.get(key)) {
              resultEqual = true;
            }
          }
          socketHandler.cutOffHerHead(roomId, userName, resultEqual);
          votes = new HashMap<>();
          roomList.get(i).setVotes(votes);
          break;
        }
      }
      return "executed";
    } catch (Exception e) {
      return "executeFail";
    }
  }
}
