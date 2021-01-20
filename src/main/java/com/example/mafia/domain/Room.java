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
  Boolean roomStatus;
  HashMap<String, Integer> votes;
  HashMap<String, Integer> zombie;
  List<String> mafia;
}
