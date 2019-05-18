package com.jazwa.javadev.controller;

import com.jazwa.javadev.model.Participant;
import com.jazwa.javadev.model.Role;
import com.jazwa.javadev.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/participants")
public class ParticipantController {
    @Autowired
    ParticipantService participantService;

    @GetMapping
    ResponseEntity<Set<Participant>> getAllParticipants(){
        Set<Participant> participants = participantService.getAll();
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/{id}")
    ResponseEntity<Participant> getParticipant(@PathVariable String id){
        Long partId = Long.parseLong(id);
        return ResponseEntity.of(participantService.getById(partId));
    }

    @GetMapping(params = "role")
    ResponseEntity<Set<Participant>> getParticipantByRole(@RequestParam String role){
        Role requestRole;
        try{
            requestRole = Role.valueOf(role.toUpperCase());
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(participantService.getByRole(requestRole));
    }
}
