package com.jazwa.javadev.service;

import com.jazwa.javadev.model.CourseClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

public interface ClassService {

     Set<CourseClass> getByDate(LocalDate date);
     Optional<CourseClass> addNewClass(CourseClass courseClass);

     Optional<CourseClass> cancelClass(Long id);
     Optional<CourseClass> cancelClass(CourseClass courseClass);
     Optional<CourseClass> cancelClass(LocalDateTime classDate);

     Set<CourseClass> cancelClasses(LocalDate classesDate);

     CourseClass save(CourseClass courseClass);

     Set<CourseClass> getAll();

     Optional<CourseClass> getById(Long id);

}
