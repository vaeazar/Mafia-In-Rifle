package com.example.mafia.repository;

import com.example.mafia.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity,String> {
  public long countByUserid(String userid);
  public long countByNickname(String nickname);
  public UserEntity findDistinctByUserid(String userid);
  public int deleteDistinctByUserid(String userid);
}
