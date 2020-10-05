package com.example.mafia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChatController {
  @RequestMapping("/MafiaChat")
  public ModelAndView MafiaChat() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("MafiaChat");
    return mv;
  }
}