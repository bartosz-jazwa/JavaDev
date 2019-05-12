package com.jazwa.javadev;

import com.jazwa.javadev.model.CourseClass;
import com.jazwa.javadev.model.Participant;
import com.jazwa.javadev.repository.ClassRepo;
import com.jazwa.javadev.repository.ParticipantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

@SpringBootApplication
public class JavaDevApplication implements CommandLineRunner {
    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    ParticipantRepo participantRepo;

    @Autowired
    ClassRepo classRepo;
    public static void main(String[] args) {
        SpringApplication.run(JavaDevApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Participant participant = new Participant();
        participant.setIndex(1);
        participant.setEmail("qwe");
        participant.setPassword(encoder.encode("asd"));
        participantRepo.save(participant);

        Participant participant1 = new Participant();
        participant1.setIndex(2);
        participant1.setEmail("xxx");
        participant1.setPassword(encoder.encode("www"));
        participantRepo.save(participant1);

        LocalDate date = LocalDate.of(2019,1,1);
        LocalTime time = LocalTime.of(12,0);
        CourseClass class1 = new CourseClass(LocalDateTime.of(date,time),"java","od podstaw","Jan Kowalski");
        //class1.addParticipants(Arrays.asList(participant,participant1));
        date = LocalDate.now();

        CourseClass class2 = new CourseClass(LocalDateTime.of(date,time),"spring","security","Adam Nowak");
        //class2.addParticipants(Arrays.asList(participant1,participant));
        classRepo.save(class1);
        classRepo.save(class2);


    }
}
