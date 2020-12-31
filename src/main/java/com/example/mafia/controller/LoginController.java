package com.example.mafia.controller;

import com.example.mafia.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login")
public class LoginController {
  @Autowired
  LoginService loginService;

  @GetMapping("/loginForm")
  public String loginForm() {
    return "loginForm";
  }

  @ResponseBody
  @PostMapping("/login.do")
  public String login(String userid, String password) {
    return Boolean.toString(loginService.login(userid, password));
  }
}
