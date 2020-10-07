package com.example.mafia.controller;

import com.example.mafia.service.UserService;
import com.example.mafia.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController {
  @Autowired
  UserService userService;

  private String resultViewName = "userResult";

  @PostMapping("/insert")
  public ModelAndView insert(UserEntity vo) {
    ModelAndView mav = new ModelAndView(resultViewName);
    mav.addObject("result",userService.insert(vo));
    return mav;
  }

  @PostMapping("/update")
  public ModelAndView update(UserEntity vo) {
    ModelAndView mav = new ModelAndView(resultViewName);
    mav.addObject("result",userService.update(vo));
    return mav;
  }

  @GetMapping("/delete")
  public ModelAndView delete(String userid) {
    ModelAndView mav = new ModelAndView(resultViewName);
    mav.addObject("result",userService.delete(userid));
    return mav;
  }

  @GetMapping("/listAll")
  public ModelAndView listAll() {
    ModelAndView mav = new ModelAndView("userlistjsp");
    mav.addObject("userlist",userService.listAll());
    return mav;
  }

  @GetMapping("/selectOne")
  public ModelAndView selectOne(String userid) {
    ModelAndView mav = new ModelAndView("userinfo");
    mav.addObject("uservo",userService.selectOne(userid));
    return mav;
  }

  @ResponseBody
  @RequestMapping(value = "/hasId",method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
  public String hasId(String userid) {
    return Boolean.toString(userService.hasId(userid));
  }
}
