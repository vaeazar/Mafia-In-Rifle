package com.example.mafia.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "MafiaMessage")
public class MafiaMessage {
  private String userId;
  private String message;
}