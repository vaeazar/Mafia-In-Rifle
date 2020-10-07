package com.example.mafia.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document(collection = "UserInfo")
public class UserEntity {
  private String userid;
  private String username;
  private String password;
  private int citizenWin;
  private int citizenLose;
  private int mafiaWin;
  private int mafiaLose;
  public double citizenRate() {
    return ((double)citizenWin)/(citizenLose+citizenWin);
  }
  public double mafiaRate() {
    return ((double)mafiaWin)/(mafiaLose+mafiaWin);
  }
}
