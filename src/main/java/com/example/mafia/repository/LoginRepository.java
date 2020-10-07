package com.example.mafia.repository;

import com.example.mafia.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoginRepository extends MongoRepository<UserEntity,String> {
  public UserEntity findDistinctByUserid(String userid);
}
