package com.example.mafia.repository;

import com.example.mafia.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity,String> {
  public boolean existsByUserid(String userid);
  public UserEntity findDistinctByUserid(String userid);
  public int deleteDistinctByUserid(String userid);
}
