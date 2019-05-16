package com.jazwa.javadev.repository;

import com.jazwa.javadev.model.CourseClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ClassRepo extends JpaRepository<CourseClass, Long> {
    Set<CourseClass> findCourseClassesByStartTimeIsAfterAndStartTimeIsBefore(LocalDateTime startDate,LocalDateTime finishDate);
    Optional<CourseClass> deleteCourseClassById(Long id);
    Optional<CourseClass> deleteCourseClassByStartTime(LocalDateTime startTime);
}
