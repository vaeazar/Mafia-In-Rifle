package com.example.mafia.controller;

import com.mongodb.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ChatController {

  @RequestMapping("/mafiaChat")
  public ModelAndView MafiaChat(HttpServletRequest request) {
    String url = request.getHeader("referer");
    ModelAndView mv = new ModelAndView();
    if (url == null) {
      mv.setViewName("room");
      return mv;
    }
    mv.setViewName("mafiaChat");
    return mv;
  }
}