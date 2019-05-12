package com.jazwa.javadev.repository;

import com.jazwa.javadev.model.CourseClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface ClassRepo extends JpaRepository<CourseClass, LocalDateTime> {
    //Set<CourseClass> findCourseClassesByStartTimeIsIn(LocalDateTime startDate,LocalDateTime finishDate);
    Set<CourseClass> findCourseClassesByStartTimeIsAfterAndStartTimeIsBefore(LocalDateTime startDate,LocalDateTime finishDate);
    //Set<CourseClass> findCourseClassesByStartTimeWithin(LocalDateTime startDate,LocalDateTime finishDate);
    CourseClass deleteByStartTime(LocalDateTime startTime);
}
