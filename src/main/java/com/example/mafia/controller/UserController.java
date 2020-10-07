package com.example.mafia.controller;

import com.example.mafia.entity.UserEntity;
import com.example.mafia.service.UserService;
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

  @ResponseBody
  @PostMapping("/insert")
  public String insert(@RequestParam("userid")String userid
                        ,@RequestParam("username")String username
                        ,@RequestParam("password")String password) {
    UserEntity entity = new UserEntity();
    entity.setUsername(username);
    entity.setUserid(userid);
    entity.setPassword(password);
    return Boolean.toString(userService.insert(entity));
  }

  @PostMapping("/update")
  public ModelAndView update(UserEntity entity) {
    ModelAndView mav = new ModelAndView(resultViewName);
    mav.addObject("result",userService.update(entity));
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
    mav.addObject("userentity",userService.selectOne(userid));
    return mav;
  }

  @ResponseBody
  @RequestMapping(value = "/hasId",method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
  public String hasId(String userid) {
    return Boolean.toString(userService.hasId(userid));
  }
}
