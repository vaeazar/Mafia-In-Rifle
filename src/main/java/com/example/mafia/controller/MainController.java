package com.example.mafia.controller;

import com.example.mafia.dao.MemberDao;
import com.example.mafia.dao.RoomDao;
import com.example.mafia.domain.Member;
import com.example.mafia.domain.Room;
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

  List<Room> roomArrayList = new ArrayList<Room>();
  static int roomNumber = 0;

  @Autowired
  SocketHandler socketHandler;

  @Autowired
  RoomDao roomDao;

  @Autowired
  MemberDao memberDao;

  @RequestMapping("/")
  public ModelAndView Index() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("lunchRoom");
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
    if(roomName != null && !roomName.trim().equals("")) {
      Room room = new Room();
      room.setRoomNumber(0);
      room.setRoomName(roomName);
      room.setRoomCount(0);
      room.setRoomStatus("wait");
      String uniqueValue = UUID.randomUUID().toString().substring(0,8);
      uniqueValue += System.currentTimeMillis();
      room.setRoomId(uniqueValue);
      SocketHandler.setRoomId(uniqueValue);

      roomDao.insert(room);

      roomArrayList.add(room);
      mv.addObject("roomName", params.get("roomName"));
      mv.addObject("roomNumber", roomNumber);
      mv.addObject("roomId", uniqueValue);
      mv.addObject("userId", params.get("userId"));
      mv.setViewName("mafiaChat");
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
    return roomDao.selectRoomInfoList();
  }

//  /**
//   * 방 정보가져오기
//   * @param params
//   * @return
//   */
//  @RequestMapping("/getRoom")
//  public @ResponseBody List<Room> getRoom(@RequestParam HashMap<Object, Object> params){
//    List <Room> sendList = new ArrayList<>();
//    for (int i = 0; i < roomList.size(); i++) {
//      Room room = roomList.get(i);
//      room.setRoomCount(socketHandler.getRoomCount(room.getRoomId()));
//      roomList.set(i,room);
//      if (socketHandler.getRoomCount(room.getRoomId()) > 0) {
//        sendList.add(room);
//      }
//    }
//    return sendList;
//  }

  /**
   * 방 시작
   * @param params
   * @return
   */
  @RequestMapping("/setRoomStart")
  public @ResponseBody void setRoomStart(@RequestParam HashMap<Object, Object> params){
    HashMap<String, String> jobs = new HashMap<>();
    String roomId = String.valueOf(params.get("roomId"));
    Room startedRoom = roomDao.selectRoomInfo(roomId);
    if (!ObjectUtils.isEmpty(startedRoom)) {
      startedRoom.setRoomStatus("start");
      socketHandler.giveJobsDGJung(roomId);
      roomDao.changeRoomStatus(startedRoom);
    }
  }

  /**
   * 방 시작
   * @param params
   * @return
   */
  @RequestMapping("/getMafiaList")
  public @ResponseBody void getMafiaList(@RequestParam HashMap<Object, Object> params){
    for (int i = 0; i < roomArrayList.size(); i++) {
      Room room = roomArrayList.get(i);
      if (room.getMafia() == null) room.setMafia(new ArrayList<>());
      if (room.getRoomId().equals(params.get("roomId"))) {
        room.setRoomStatus("start");
        List<String> mafiaList = room.getMafia();
        mafiaList.add(String.valueOf(params.get("userId")));
        roomArrayList.set(i,room);
      }
    }
  }

//  /**
//   * 방 제거
//   * @param params
//   * @return
//   */
//  @RequestMapping("/delRoom")
//  public @ResponseBody void delRoom(@RequestParam HashMap<Object, Object> params){
//    for (int i = 0; i < roomList.size(); i++) {
//      Room room = roomList.get(i);
//      if (room.getRoomId().equals(params.get("roomId"))) {
//        roomList.remove(i);
//      }
//    }
//  }

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

    Room existChk = roomDao.selectRoomInfo(roomId);
    //List<Room> new_list = roomArrayList.stream().filter(o->o.getRoomNumber()==roomNumber).collect(Collectors.toList());
    if(existChk != null) {
      mv.addObject("roomName", existChk.getRoomName());
      mv.addObject("roomNumber", existChk.getRoomNumber());
      mv.addObject("roomId", existChk.getRoomId());
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
      for(int i=0; i< roomArrayList.size(); i++) {
        String getRoomId = roomArrayList.get(i).getRoomId();
        if (!StringUtils.isEmpty(getRoomId) && getRoomId.equals(roomId)) {
          roomArrayList.remove(i);
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
    for (int i=0;i< roomArrayList.size();i++) {
      String getRoomId = roomArrayList.get(i).getRoomId();
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
    List<String> memberList = memberDao.selectAliveMemberNames(roomId);
    try {
      if (memberList == null) memberList = new ArrayList<>();
      memberListString.put("memberList",memberList);
      return memberListString.toJSONString();
    } catch (Exception e) {
      memberListString.put("memberList",new ArrayList<>());
      return memberListString.toJSONString();
    }
  }

  @RequestMapping("/getCivilNames")
  public @ResponseBody
  String getCivilNames(@RequestParam HashMap<Object, Object> params) {
    String roomId = StringUtils.isEmpty(params.get("roomId")) == true ? "" : params.get("roomId").toString();
    JSONObject memberListString = new JSONObject();

    if (roomId.equals("")) {
      return memberListString.toJSONString();
    }
    List<String> memberList = memberDao.selectCivil(roomId);

    try {
      if (memberList == null) memberList = new ArrayList<>();
      memberListString.put("civilList",memberList);
      return memberListString.toJSONString();
    } catch (Exception e) {
      memberListString.put("civilList",new ArrayList<>());
      return memberListString.toJSONString();
    }
  }

  @RequestMapping("/BBalGangEDa")
  public @ResponseBody
  String BBalGangEDa(@RequestParam HashMap<Object, Object> params) {
    String roomId = StringUtils.isEmpty(params.get("roomId")) == true ? "" : params.get("roomId").toString();
    String playerId = StringUtils.isEmpty(params.get("playerId")) == true ? "" : params.get("playerId").toString();

    try {
      for(int i=0; i< roomArrayList.size(); i++) {
        String getRoomId = roomArrayList.get(i).getRoomId();
        if (!StringUtils.isEmpty(getRoomId) && getRoomId.equals(roomId)) {
          HashMap<String, Integer> votes = roomArrayList.get(i).getVotes();
          if (CollectionUtils.isEmpty(votes)) {
            votes = new HashMap<>();
            votes.put(playerId,1);
          } else if (votes.get(playerId) == null){
            votes.put(playerId,1);
          } else {
            votes.put(playerId, votes.get(playerId) + 1);
          }
          roomArrayList.get(i).setVotes(votes);
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
  String cutOffHerHead(@RequestParam HashMap<Object, Object> params) {
    String roomId = StringUtils.isEmpty(params.get("roomId")) == true ? "" : params.get("roomId").toString();
    try {
      int userCount = 0;
      String userName = "";
      Boolean resultEqual = true;
      for(int i=0; i< roomArrayList.size(); i++) {
        String getRoomId = roomArrayList.get(i).getRoomId();
        if (!StringUtils.isEmpty(getRoomId) && getRoomId.equals(roomId)) {
          HashMap<String, Integer> votes = roomArrayList.get(i).getVotes();
          if (CollectionUtils.isEmpty(votes)) {
            socketHandler.cutOffHerHead(roomId, userName, resultEqual);
            break;
          }
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
          roomArrayList.get(i).setVotes(votes);
          break;
        }
      }
      return "executed";
    } catch (Exception e) {
      return "executeFail";
    }
  }

  @RequestMapping("/mafiaKill")
  public @ResponseBody
  String mafiaKill(@RequestParam HashMap<Object, Object> params) {
    String roomId = StringUtils.isEmpty(params.get("roomId")) == true ? "" : params.get("roomId").toString();
    try {
      int userCount = 0;
      ArrayList<String> equalList = new ArrayList<>();
      int equalIndex = 0;
      String userName = "";
      Boolean resultEqual = true;
      for(int i=0; i< roomArrayList.size(); i++) {
        String getRoomId = roomArrayList.get(i).getRoomId();
        if (!StringUtils.isEmpty(getRoomId) && getRoomId.equals(roomId)) {
          HashMap<String, Integer> votes = roomArrayList.get(i).getVotes();
          if (CollectionUtils.isEmpty(votes)) {
            socketHandler.mafiaKill(roomId, userName);
            break;
          }
          Iterator<String> keys = votes.keySet().iterator();
          while( keys.hasNext() ){
            String key = keys.next();
            if (userCount < votes.get(key)) {
              userCount = votes.get(key);
              userName = key;
              resultEqual = false;
              if (equalIndex != 0) {
                equalList = new ArrayList<>();
                equalIndex = 0;
              }
            } else if (userCount == votes.get(key)) {
              equalList.add(equalIndex,key);
              equalIndex++;
              resultEqual = true;
            }
          }

          if (resultEqual) {
            equalList.add(userName);
            Collections.shuffle(equalList);
            userName = equalList.get(0);
          }
          socketHandler.mafiaKill(roomId, userName);
          votes = new HashMap<>();
          roomArrayList.get(i).setVotes(votes);
          break;
        }
      }
      return "executed";
    } catch (Exception e) {
      return "executeFail";
    }
  }

  @RequestMapping("/mafiaVote")
  public @ResponseBody
  String mafiaVote(@RequestParam HashMap<Object, Object> params) {
    String roomId = StringUtils.isEmpty(params.get("roomId")) == true ? "" : params.get("roomId").toString();
    String playerId = StringUtils.isEmpty(params.get("playerId")) == true ? "" : params.get("playerId").toString();

    try {
      for(int i=0; i< roomArrayList.size(); i++) {
        String getRoomId = roomArrayList.get(i).getRoomId();
        if (!StringUtils.isEmpty(getRoomId) && getRoomId.equals(roomId)) {
          HashMap<String, Integer> votes = roomArrayList.get(i).getVotes();
          if (CollectionUtils.isEmpty(votes)) {
            votes = new HashMap<>();
            votes.put(playerId,1);
          } else if (votes.get(playerId) == null){
            votes.put(playerId,1);
          } else {
            votes.put(playerId, votes.get(playerId) + 1);
          }
          roomArrayList.get(i).setVotes(votes);
          break;
        }
      }
      return "mafiaVoteComplete";
    } catch (Exception e) {
      return "mafiaVoteFail";
    }
  }
}
