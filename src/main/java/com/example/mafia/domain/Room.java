package com.example.mafia.domain;

import lombok.Data;

@Data
public class Room {
  int roomNumber;
  String roomName;
  String roomId;
  int roomCount;
  Boolean roomStatus;
}
