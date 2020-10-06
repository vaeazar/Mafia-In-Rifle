package com.example.mafia.controller;

import com.example.mafia.service.UserService;
import com.example.mafia.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController {
  @Autowired
  UserService userService;

  @PostMapping("/insert")
  public ModelAndView insert(UserVO vo) {
    ModelAndView mav = new ModelAndView("joinResult");
    mav.addObject("result",userService.insert(vo));
    return mav;
  }

  @PostMapping("/update")
  public
}
