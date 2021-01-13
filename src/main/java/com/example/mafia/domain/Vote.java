package com.example.mafia.domain;

import lombok.Data;

@Data
public class Vote {
  String playerId;
  String playerName;
  int voteCount;
}
