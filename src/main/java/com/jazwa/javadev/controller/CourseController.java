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
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/classes")
public class CourseController {
    @Autowired
    ClassRepo classRepository;
    @Autowired
    ClassService classService;

    @GetMapping
    ResponseEntity<List<CourseClass>> getAllClasses(){
        List<CourseClass> classList = classRepository.findAll();
        if (classList.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(classList);
    }

    @GetMapping(params = "date")
    ResponseEntity<Set<CourseClass>> getClassesByDate(@RequestParam String date){

        LocalDate courseDate = LocalDate.parse(date);
        Set<CourseClass> courseClasses = classService.findByDate(courseDate);
        if (courseClasses.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courseClasses);
    }

    @GetMapping("/{id}")
    ResponseEntity<CourseClass> getOneCourseClass(@PathVariable String id){
        Long classId = Long.parseLong(id);
        return ResponseEntity.of(classRepository.findById(classId));
    }

    @PostMapping
    ResponseEntity<CourseClass> addNewClass(@RequestBody CourseClass courseClass){

        Optional<CourseClass> result = classService.addNewClass(courseClass);

        if (result.isPresent()){
            return ResponseEntity.ok(result.get());
        }else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<CourseClass> deleteClass(@PathVariable String id){

        Long classId = Long.parseLong(id);

        Optional<CourseClass> deleteResult = classService.cancelClass(classId);
        if (!deleteResult.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(deleteResult);

    }

    @DeleteMapping("/{date}")
    ResponseEntity<Set<CourseClass>> deleteClasses(@PathVariable String date){
        LocalDate localDate = LocalDate.parse(date);

        Set<CourseClass> classSet = classService.cancelClasses(localDate);
        if (classSet.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(classSet);
    }

    @PutMapping("/{id}")
    ResponseEntity<CourseClass> updateClass(@PathVariable String id,
                                            @RequestParam String date,
                                            @RequestParam String time,
                                            @RequestParam String title,
                                            @RequestParam(required = false) String description,
                                            @RequestParam(required = false) String tutor){

        LocalDateTime localDateTime;
        Long classId;
        try{
            localDateTime = LocalDateTime.of(LocalDate.parse(date),LocalTime.parse(time));
            classId = Long.parseLong(id);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

        Optional<CourseClass> courseClass = classRepository.findById(classId);
        courseClass.ifPresent(course -> {
            course.setStartTime(localDateTime);
            course.setTitle(title);
            course.setDescription(description);
            course.setTutor(tutor);
            classRepository.save(course);
        });

        return ResponseEntity.of(courseClass);
    }
}
