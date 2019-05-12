package com.jazwa.javadev.controller;

import com.jazwa.javadev.model.Participant;
import com.jazwa.javadev.repository.ParticipantRepo;
import com.jazwa.javadev.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;

@RestController
public class ParticipantController {
    @Autowired
    ParticipantService participantService;

    @GetMapping("/participants")
    ResponseEntity<Set<Participant>> getAllParticipants(){
        Set<Participant> participants = participantService.findAll();
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/participants/{email}")
    ResponseEntity<Participant> getParticipantByEmail(@PathVariable String email){

        //Participant participant = participantService.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        return ResponseEntity.of(participantService.findByEmail(email));
    }

    @GetMapping("/participants/{index}")
    ResponseEntity<Participant> getParticipantByIndex(@PathVariable String index){
        Integer indexParam = Integer.parseInt(index);
        //Participant participant = participantService.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        return ResponseEntity.of(participantService.findByIndex(indexParam));
    }
}
