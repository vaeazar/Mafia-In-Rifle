package com.example.mafia.repository;

import com.example.mafia.domain.MafiaMessage;
import com.example.mafia.domain.PlayerJob;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.HashMap;
import java.util.List;

public interface PlayerJobMongoDBRepository extends MongoRepository<PlayerJob, String> {
  public List<PlayerJob> findByPlayerId(String playerId);
}
