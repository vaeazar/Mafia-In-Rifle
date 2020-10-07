package com.example.mafia.service;

import com.example.mafia.entity.UserEntity;
import com.example.mafia.repository.LoginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class LoginService {
  @Autowired
  LoginRepository loginRepository;

  @Autowired
  HttpSession session;

  Logger log = LoggerFactory.getLogger(LoginService.class);

  public boolean login(String userid,String password) {
    boolean result = false;
    try {
      UserEntity entity = loginRepository.findDistinctByUserid(userid);
      if(entity.getPassword().equals(password)) {
        result = true;
        session.setAttribute("UserEntity",entity);
      }
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    return result;
  }

}
