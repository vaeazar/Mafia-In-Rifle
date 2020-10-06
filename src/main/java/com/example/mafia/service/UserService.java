package com.example.mafia.service;

import com.example.mafia.repository.UserDAO;
import com.example.mafia.vo.UserVO;
import com.google.gson.Gson;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
  @Autowired
  UserDAO userDAO;

  Logger log = LoggerFactory.getLogger(UserDAO.class);

  public boolean insert(UserVO vo) {
   boolean result = false;
    try {
      userDAO.insert(vo);
      result = true;
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    return result;
  }

  public boolean update(UserVO vo) {
    boolean result = false;
    try {
      userDAO.update(vo);
      result = true;
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    return result;
  }

  public boolean delete(String userid) {
    boolean result = false;
    try {
      userDAO.delete(userid);
      result = true;
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    return result;
  }

  public List<UserVO> listAll() {
    List<UserVO> list = new ArrayList<>();
    try {
      MongoCursor<Document> cursor = userDAO.listAll();
      while(cursor.hasNext()) {
        String json = cursor.next().toJson();
        Gson gson = new Gson();
        UserVO vo = gson.fromJson(json,UserVO.class);
        list.add(vo);
      }
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    return list;
  }

  public UserVO selectOne(String userid) {
    UserVO vo = new UserVO();
    try {
      Document document = userDAO.selectOne(userid);
      String json = document.toJson();
      Gson gson = new Gson();
      vo = gson.fromJson(json,UserVO.class);
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    return vo;
  }

  public boolean hasId(String userid) {
    boolean result = false;
    try {
      int cnt = (int)userDAO.countByUserid(userid);
      if(cnt == 1) {
        result = true;
      }
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    return result;
  }

}
