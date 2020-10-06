package com.example.mafia.service;

import com.example.mafia.repository.LoginDAO;
import com.example.mafia.vo.UserVO;
import com.google.gson.Gson;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
  @Autowired
  LoginDAO loginDAO;

  Logger log = LoggerFactory.getLogger(LoginService.class);

  public boolean login(String userid,String password) {
    boolean result = false;
    try {
     Document document = loginDAO.login(userid,password);
     String json = document.toJson();
     Gson gson = new Gson();
     UserVO vo = gson.fromJson(json,UserVO.class);
     if(password != null) {
       if (password.equals(vo.getPassword())) {
         result = true;
       }
     }
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    return result;
  }

}
