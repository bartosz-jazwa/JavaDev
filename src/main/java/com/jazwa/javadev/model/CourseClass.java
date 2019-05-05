package com.jazwa.javadev.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
public class CourseClass {
    @Id
    private LocalDateTime startTime;
    private String title;
    private String description;
    private String tutor;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Participant> participants;

    public void addParticipant(Participant participant){
        this.participants.add(participant);
    }
}
