package com.jazwa.javadev.repository;

import com.jazwa.javadev.model.CourseClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ClassRepo extends JpaRepository<CourseClass, LocalDateTime> {
}
