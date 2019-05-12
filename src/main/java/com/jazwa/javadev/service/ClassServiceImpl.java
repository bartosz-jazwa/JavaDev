package com.jazwa.javadev.service;

import com.jazwa.javadev.model.CourseClass;
import com.jazwa.javadev.repository.ClassRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

@Service
public class ClassServiceImpl implements ClassService {
    @Autowired
    ClassRepo classRepo;
    @Override
    public Set<CourseClass> findByDate(LocalDate date) {

        LocalDateTime start = LocalDateTime.of(date,LocalTime.MIN);
        LocalDateTime stop = LocalDateTime.of(date,LocalTime.MAX);

        return classRepo.findCourseClassesByStartTimeIsAfterAndStartTimeIsBefore(start,stop);

    }

    @Override
    public boolean addNewClass(CourseClass courseClass) {

        if(courseClass.getStartTime().isAfter(LocalDateTime.now())
        && !classRepo.existsById(courseClass.getStartTime())){
            classRepo.save(courseClass);
            return true;
        }

        return false;
    }

    @Override
    public Optional<CourseClass> cancelClass(CourseClass courseClass) {
        if(courseClass.getStartTime().isAfter(LocalDateTime.now())){
            return Optional.ofNullable(classRepo.deleteByStartTime(courseClass.getStartTime()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<CourseClass> cancelClass(LocalDateTime classDate) {

        if(classDate.isAfter(LocalDateTime.now())){
            return Optional.ofNullable(classRepo.deleteByStartTime(classDate));
        }

        return Optional.empty();
    }

    @Override
    public Set<CourseClass> cancelClasses(LocalDate classesDate) {
        LocalDateTime startTime = LocalDateTime.of(classesDate,LocalTime.MIN);
        LocalDateTime finishTime = LocalDateTime.of(classesDate,LocalTime.MAX);
        Set<CourseClass> foundClasses = classRepo
                .findCourseClassesByStartTimeIsAfterAndStartTimeIsBefore(startTime,finishTime);

        classRepo.deleteAll(foundClasses);

        return foundClasses;
    }
}
