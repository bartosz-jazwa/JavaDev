package com.jazwa.javadev.service;

import com.jazwa.javadev.model.CourseClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface ClassService {

     Set<CourseClass> findByDate(LocalDate date);
     boolean addNewClass(CourseClass courseClass);

     Optional<CourseClass> cancelClass(CourseClass courseClass);
     Optional<CourseClass> cancelClass(LocalDateTime classDate);

     Set<CourseClass> cancelClasses(LocalDate classesDate);
}
