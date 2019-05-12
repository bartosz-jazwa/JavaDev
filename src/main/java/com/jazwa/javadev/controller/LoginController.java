package com.jazwa.javadev.controller;

import com.jazwa.javadev.model.Participant;
import com.jazwa.javadev.service.ParticipantDetailsService;
import com.jazwa.javadev.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class LoginController {
    @Autowired
    ParticipantService participantService;
    @Autowired
    ParticipantDetailsService participantDetailsService;

    @GetMapping("/login")
    ResponseEntity<Participant> login(@RequestParam String username){

        return ResponseEntity.ok().body(participantService.findByEmail(username).orElse(new Participant()));
    }

    @GetMapping("/user")
    ResponseEntity<Participant> getCurrentUser(Authentication authentication){
        return ResponseEntity.ok().body(participantService.findByEmail(authentication.getName()).orElse(new Participant()));
    }

    @PostMapping("/login")
    ResponseEntity<Participant> postLogin(@RequestHeader String username){

        return ResponseEntity.ok().body(participantService.findByEmail(username).orElse(new Participant()));
    }
}
