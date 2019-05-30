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

import static com.jazwa.javadev.model.Role.ROLE_ADMIN;

@RestController
@RequestMapping("/participants")

public class ParticipantController {
    @Autowired
    ParticipantService participantService;
    @Autowired
    ClassService classService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping
    @Secured("ROLE_ADMIN")
    ResponseEntity<Set<Participant>> getAllParticipants() {
        Set<Participant> participants = participantService.getAll();
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/{id}")
    ResponseEntity<Participant> getParticipant(@PathVariable Long id,
                                               @AuthenticationPrincipal(expression = "participant") Participant p) {

        if (p.getRole() == ROLE_ADMIN) {
            return ResponseEntity.of(participantService.getById(id));
        } else {
            return ResponseEntity.of(participantService.getById(p.getId()));
        }
    }

    @GetMapping(params = "role")
    @Secured("ROLE_ADMIN")
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
    ResponseEntity<Set<CourseClass>> getParticipantClasses(@PathVariable Long id,
                                                           @AuthenticationPrincipal(expression = "participant") Participant p) {

        Set<CourseClass> resultClasses;
        if (p.getRole() == ROLE_ADMIN) {
            Optional<Participant> resultParticipant = participantService.getById(id);
            if (resultParticipant.isPresent()) {
                resultClasses = resultParticipant.get().getClasses();
            } else {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(resultClasses);
        } else {
            Optional<Participant> resultParticipant = participantService.getById(p.getId());
            if (resultParticipant.isPresent()) {
                resultClasses = resultParticipant.get().getClasses();
            } else {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(resultClasses);
        }
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    ResponseEntity<Participant> addNewParticipant(@RequestBody Participant participant) {
        Participant participantWithPassEncoded = participant;
        try {
            participantWithPassEncoded.setPassword(passwordEncoder.encode(participant.getPassword()));
        }catch (NullPointerException e){
            return ResponseEntity.unprocessableEntity().body(participant);
        }
        Optional<Participant> result = participantService.addNew(participantWithPassEncoded);

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<Participant> deleteParticipant(@PathVariable Long id) {
        Long requestId;
        try {
            requestId = id;
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
                                                  @RequestParam(required = false) String fieldOfStudy,
                                                  @AuthenticationPrincipal(expression = "participant") Participant p) {

        Optional<Participant> participant;

        if (p.getRole() == ROLE_ADMIN) {
            participant = participantService.getById(id);
        } else {
            participant = participantService.getById(p.getId());
        }
        participant.ifPresent(particip -> {
            particip.setIndex(index);
            particip.setEmail(email);
            particip.setName(name);
            particip.setSurname(surname);
            particip.setYearOfStudy(yearOfStudy);
            particip.setFieldOfStudy(fieldOfStudy);
            participantService.save(particip);
        });

        return ResponseEntity.of(participant);
    }

    @PatchMapping(value = "/{id}/classes", params = "courseId")
    ResponseEntity<Participant> updateParticipantsClasses(@PathVariable Long id,
                                                          @RequestParam Long courseId,
                                                          @AuthenticationPrincipal(expression = "participant") Participant p) {


        Optional<CourseClass> classOptional = classService.getById(courseId);
        Optional<Participant> participant;

        switch (p.getRole()){
            case ROLE_ADMIN:
                participant = participantService.getById(id);
                break;
            default:
                participant = participantService.getById(p.getId());
                break;
        }

        if (!classOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        classOptional.ifPresent(course -> {

            participant.ifPresent(particip -> {
                course.addParticipant(particip);
                classService.save(course);
            });

        });

        return ResponseEntity.of(participant);
    }

    @DeleteMapping(value = "/{id}/classes", params = "courseId")
    ResponseEntity<Participant> cancelParticipantsClass(@PathVariable Long id,
                                                        @RequestParam Long courseId,
                                                        @AuthenticationPrincipal(expression = "participant") Participant p) {

        Optional<CourseClass> classOptional = classService.getById(courseId);
        Optional<Participant> participant;

        switch (p.getRole()){
            case ROLE_ADMIN:
                participant = participantService.getById(id);
                break;
            default:
                participant = participantService.getById(p.getId());
                break;
        }

        AtomicReference<ResponseEntity<Participant>> response = new AtomicReference<>(ResponseEntity.of(participant));
        participant.ifPresent(particip -> {
            classOptional.ifPresent(course -> {
                if (particip.getClasses().contains(course)) {
                    course.removeParticipant(particip);
                    particip.getClasses().remove(course);
                    response.set(ResponseEntity.ok(particip));
                } else {
                    response.set(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                }
                classService.save(course);
            });
        });

        return response.get();
    }
}
