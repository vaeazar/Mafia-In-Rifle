package com.example.mafia.controller;

import com.example.mafia.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/login")
public class LoginController {
  @Autowired
  LoginService loginService;

  @GetMapping("/loginForm")
  public String loginForm() {
    return "loginForm";
  }

  @PostMapping("/login.do")
  public ModelAndView login(String userid, String password) {
    ModelAndView mav = new ModelAndView("loginResult");
    String result = Boolean.toString(loginService.login(userid, password));
    mav.addObject("result",result);
    return mav;
  }
}