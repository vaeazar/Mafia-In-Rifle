package com.example.mafia.service;

import com.example.mafia.entity.UserEntity;
import com.example.mafia.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
  @Autowired
  UserRepository userRepository;

  Logger log = LoggerFactory.getLogger(UserService.class);

  public boolean insert(UserEntity entity) {
   boolean result = false;
    try {
      userRepository.insert(entity);
      result = true;
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    return result;
  }

  public boolean update(UserEntity entity) {
    boolean result = false;
    try {
      userRepository.save(entity);
      result = true;
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    return result;
  }

  public boolean delete(String userid) {
    boolean result = false;
    try {
      userRepository.deleteDistinctByUserid(userid);
      result = true;
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    return result;
  }

  public List<UserEntity> listAll() {
    List<UserEntity> list = new ArrayList<>();
    try {
      list = userRepository.findAll();
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    return list;
  }

  public UserEntity selectOne(String userid) {
    UserEntity entity = new UserEntity();
    try {
      entity = userRepository.findDistinctByUserid(userid);
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    return entity;
  }

  public boolean hasId(String userid) {
    boolean result = false;
    try {
      result = userRepository.existsByUserid(userid);
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    return result;
  }

}
