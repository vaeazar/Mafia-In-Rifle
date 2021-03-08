package com.example.mafia.domain;

import java.util.HashMap;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Member {
  String memberId;
  String memberName;
  String memberRoomId;
  String memberAdminYN;
  String memberStatus;
  String memberJob;
  int sessionIdx;
}
