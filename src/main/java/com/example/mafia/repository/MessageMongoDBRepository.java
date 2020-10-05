package com.example.mafia.repository;

import com.example.mafia.domain.MafiaMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageMongoDBRepository extends MongoRepository<MafiaMessage, String > {
  public List<MafiaMessage> findByUserId(String id);
  public List<MafiaMessage> findBymessage(String message);
}
