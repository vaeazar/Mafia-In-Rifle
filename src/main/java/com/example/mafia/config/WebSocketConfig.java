package com.example.mafia.config;

import com.example.mafia.handler.LunchSocketHandler;
import com.example.mafia.handler.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  @Autowired
  SocketHandler socketHandler;

  @Autowired
  LunchSocketHandler lunchSocketHandler;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(socketHandler, "/chating/{roomId}");
    registry.addHandler(lunchSocketHandler, "/lunchChating/{roomId}");
  }
}
