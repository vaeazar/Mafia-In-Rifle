package com.example.mafia.domain;

import java.util.HashMap;
import java.util.List;
import lombok.Data;

@Data
public class Member {
  String memberId;
  String memberName;
  String memberRoomId;
  String memberAdminYN;
  String memberStatus;
  int sessionIdx;
}
