package com.jazwa.javadev.controller;

import com.jazwa.javadev.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @Autowired
    ParticipantService participantService;

    @GetMapping("/log")
    String login(){
        return "login";
    }
}
