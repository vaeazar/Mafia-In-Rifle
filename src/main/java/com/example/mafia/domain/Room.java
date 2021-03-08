package com.example.mafia.domain;

import java.util.HashMap;
import java.util.List;
import lombok.Data;

@Data
public class Room {
  int roomNumber;
  String roomName;
  String roomId;
  int roomCount;
  String roomStatus;
  String roomVoteYN;
  String roomMafiaVoteYN;
  int sessionIdx;
  HashMap<String, Integer> votes;
  HashMap<String, Integer> zombie;
  List<String> mafia;
  HashMap<String, String> jobs;
}
