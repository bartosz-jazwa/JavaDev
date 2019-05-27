package com.jazwa.javadev.controller;

import com.jazwa.javadev.model.CourseClass;
import com.jazwa.javadev.model.Participant;
import com.jazwa.javadev.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/classes")
public class CourseController {

    @Autowired
    ClassService classService;

    @GetMapping
    ResponseEntity<Set<CourseClass>> getAllClasses() {
        Set<CourseClass> classSet = classService.getAll();
        if (classSet.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(classSet);
    }

    @GetMapping(params = "date")
    ResponseEntity<Set<CourseClass>> getClassesByDate(@RequestParam String date) {
        LocalDate courseDate;
        try {
            courseDate = LocalDate.parse(date);
        }catch (DateTimeParseException c){
            return ResponseEntity.badRequest().build();
        }

        Set<CourseClass> courseClasses = classService.getByDate(courseDate);
        if (courseClasses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courseClasses);
    }

    @GetMapping("/{id}")
    ResponseEntity<CourseClass> getOneCourseClass(@PathVariable Long id) {
        Long classId = id;
        return ResponseEntity.of(classService.getById(classId));
    }

    @GetMapping("/{id}/participants")
    ResponseEntity<Set<Participant>> getClassParticipants(@PathVariable Long id) {
        Long classId = id;
        Set<Participant> participants = new HashSet<>();
        Optional<CourseClass> courseClass = classService.getById(classId);
        if (courseClass.isPresent()) {
            participants = courseClass.get().getParticipants();
        }
        if (participants.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(participants);
    }

    @PostMapping
    ResponseEntity<CourseClass> addNewClass(@RequestBody CourseClass courseClass) {

        Optional<CourseClass> result = classService.addNewClass(courseClass);

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<CourseClass> deleteClass(@PathVariable(name = "id") Long id) {

        Long classId = id;

        Optional<CourseClass> deleteResult = classService.cancelClass(classId);
        if (!deleteResult.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(deleteResult);

    }

    @DeleteMapping
    ResponseEntity<Set<CourseClass>> deleteClasses(@RequestParam(name = "date") String date) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date);
        }catch (DateTimeParseException c){
            return ResponseEntity.badRequest().build();
        }

        Set<CourseClass> classSet = classService.cancelClasses(localDate);
        if (classSet.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(classSet);
    }

    @PutMapping("/{id}")
    ResponseEntity<CourseClass> updateClass(@PathVariable Long id,
                                            @RequestParam String date,
                                            @RequestParam String time,
                                            @RequestParam String title,
                                            @RequestParam(required = false) String description,
                                            @RequestParam(required = false) String tutor) {

        LocalDateTime localDateTime;
        Long classId;
        try {
            localDateTime = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));
            classId = id;
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }

        Optional<CourseClass> courseClass = classService.getById(classId);
        courseClass.ifPresent(course -> {
            course.setStartTime(localDateTime);
            course.setTitle(title);
            course.setDescription(description);
            course.setTutor(tutor);
            classService.save(course);
        });

        return ResponseEntity.of(courseClass);
    }
}
