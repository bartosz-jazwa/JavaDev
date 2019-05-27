package com.jazwa.javadev.controller;

import com.jazwa.javadev.model.CourseClass;
import com.jazwa.javadev.model.Participant;
import com.jazwa.javadev.model.Role;
import com.jazwa.javadev.service.ClassService;
import com.jazwa.javadev.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/participants")

public class ParticipantController {
    @Autowired
    ParticipantService participantService;
    @Autowired
    ClassService classService;
    @Autowired
    PasswordEncoder bcrypt;

    @GetMapping
    @Secured("ROLE_ADMIN")
    ResponseEntity<Set<Participant>> getAllParticipants() {
        Set<Participant> participants = participantService.getAll();
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/{id}")
    ResponseEntity<Participant> getParticipant(@PathVariable Long id,
                                               @AuthenticationPrincipal(expression = "participant") Participant p) {

        if (p.getRole()==Role.ROLE_ADMIN){
            return ResponseEntity.of(participantService.getById(id));
        }else {
            return ResponseEntity.of(participantService.getById(p.getId()));
        }

    }

    @GetMapping(params = "role")
    ResponseEntity<Set<Participant>> getParticipantByRole(@RequestParam String role) {
        Role requestRole;
        try {
            requestRole = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(participantService.getByRole(requestRole));
    }

    @GetMapping("/{id}/classes")
    ResponseEntity<Set<CourseClass>> getParticipantClasses(@PathVariable Long id) {
        Long partId;
        try {
            partId = id;
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
        Set<CourseClass> resultClasses;
        Optional<Participant> resultParticipant = participantService.getById(partId);
        if (resultParticipant.isPresent()) {
            resultClasses = resultParticipant.get().getClasses();
        } else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultClasses);
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    ResponseEntity<Participant> addNewParticipant(@RequestBody Participant participant) {
        Participant participantWithPassEncoded = participant;
        participantWithPassEncoded.setPassword(bcrypt.encode(participant.getPassword()));
        Optional<Participant> result = participantService.addNew(participant);

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @DeleteMapping
    ResponseEntity<Participant> deleteParticipant(@RequestParam String id) {
        Long requestId;
        try {
            requestId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.of(participantService.deleteById(requestId));
    }

    @PutMapping("/{id}")
    ResponseEntity<Participant> updateParticipant(@PathVariable Long id,
                                                  @RequestParam(required = false) Integer index,
                                                  @RequestParam(required = false) String email,
                                                  @RequestParam(required = false) String name,
                                                  @RequestParam(required = false) String surname,
                                                  @RequestParam(required = false) Integer yearOfStudy,
                                                  @RequestParam(required = false) String fieldOfStudy) {
        Long requestId;
        Integer paramIndex;
        Integer paramYear;
        try {
            requestId = id;

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Participant> participant = participantService.getById(requestId);

        try {
            paramIndex = index;
            paramYear = yearOfStudy;
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        participant.ifPresent(particip -> {
            particip.setIndex(paramIndex);
            particip.setEmail(email);
            particip.setName(name);
            particip.setSurname(surname);
            particip.setYearOfStudy(paramYear);
            particip.setFieldOfStudy(fieldOfStudy);
            participantService.save(particip);
        });

        return ResponseEntity.of(participant);
    }

    @PutMapping(value = "/{id}/classes", params = "courseId")
    ResponseEntity<Participant> updateParticipantsClasses(@PathVariable Long id,
                                                          @RequestParam Long courseId) {


        Optional<CourseClass> classOptional = classService.getById(courseId);
        Optional<Participant> participant = participantService.getById(id);

        if (!classOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        classOptional.ifPresent(course -> {

            participant.ifPresent(p -> {
                course.addParticipant(p);
                classService.save(course);
            });

        });

        return ResponseEntity.of(participant);
    }

    @DeleteMapping(value = "/{id}/classes", params = "courseId")
    ResponseEntity<Participant> cancelParticipantsClass(@PathVariable Long id,
                                                        @RequestParam Long courseId){

        Optional<CourseClass> classOptional = classService.getById(courseId);
        Optional<Participant> participant = participantService.getById(id);

        AtomicReference<ResponseEntity<Participant>> response = new AtomicReference<>(ResponseEntity.of(participant));
        participant.ifPresent(p ->{
            classOptional.ifPresent(course ->{
                if(p.getClasses().contains(course)){
                    course.removeParticipant(p);
                    p.getClasses().remove(course);
                    response.set(ResponseEntity.ok(p));
                }else {
                    response.set(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                }
                classService.save(course);
            });
        });

        return response.get();
    }
}
