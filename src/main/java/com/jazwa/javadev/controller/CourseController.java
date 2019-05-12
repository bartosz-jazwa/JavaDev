package com.jazwa.javadev.controller;

import com.jazwa.javadev.model.CourseClass;
import com.jazwa.javadev.model.Participant;
import com.jazwa.javadev.repository.ClassRepo;
import com.jazwa.javadev.service.ClassService;
import com.jazwa.javadev.service.ClassServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController("/classes")
public class CourseController {
    @Autowired
    ClassRepo classRepository;
    @Autowired
    ClassService classService;

    @GetMapping()
    ResponseEntity<List<CourseClass>> getAllClasses(){
        List<CourseClass> classList = classRepository.findAll();
        if (classList.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(classList);
    }

    @GetMapping("/{date}")
    ResponseEntity<Set<CourseClass>> getClassesByDate(@PathVariable String date){

        LocalDate courseDate = LocalDate.parse(date);
        Set<CourseClass> courseClasses = classService.findByDate(courseDate);
        if (courseClasses.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courseClasses);
    }

    @PostMapping("/classes")
    ResponseEntity<CourseClass> addNewClass(@RequestBody CourseClass courseClass){

        if (classService.addNewClass(courseClass)){
            return ResponseEntity.ok(courseClass);
        }else {
            return ResponseEntity.badRequest().build();
        }

    }
    @DeleteMapping("/classes/{date}/{time}")
    ResponseEntity<CourseClass> deleteClass(@PathVariable String date,
                                            @PathVariable String time){
        LocalDate localDate = LocalDate.parse(date);
        LocalTime localTime = LocalTime.parse(time);
        LocalDateTime dateTime = LocalDateTime.of(localDate,localTime);

        Optional<CourseClass> deleteResult = classService.cancelClass(dateTime);
        if (!deleteResult.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(deleteResult);

    }

    @DeleteMapping("/classes/{date}")
    ResponseEntity<Set<CourseClass>> deleteClass(@PathVariable String date){
        LocalDate localDate = LocalDate.parse(date);

        Set<CourseClass> classSet = classService.cancelClasses(localDate);
        if (classSet.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(classSet);
    }

    @PutMapping("/classes/{date}/{time}")
    ResponseEntity<CourseClass> updateClass(@PathVariable String date,
                                            @PathVariable String time,
                                            @RequestParam String title,
                                            @RequestParam String description,
                                            @RequestParam String tutor){

        LocalDate localDate = LocalDate.parse(date);
        LocalTime localTime = LocalTime.parse(time);
        LocalDateTime classId = LocalDateTime.of(localDate,localTime);
        Optional<CourseClass> courseClass = classRepository.findById(classId);
        courseClass.ifPresent(course -> {
            course.setTitle(title);
            course.setDescription(description);
            course.setTutor(tutor);

        });

        return ResponseEntity.of(courseClass);
    }

}
