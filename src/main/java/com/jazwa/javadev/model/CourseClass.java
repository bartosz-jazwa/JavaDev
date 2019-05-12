package com.jazwa.javadev.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.util.HashSet;
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
    private Set<Participant> participants = new HashSet<>();

    public void addParticipant(Participant participant){
        this.participants.add(participant);
    }

    public void addParticipants(List<Participant> participants){
        this.participants.addAll(participants);
    }

    public CourseClass() {
    }

    public CourseClass(LocalDateTime startTime, String title, String description, String tutor) {
        this.startTime = startTime;
        this.title = title;
        this.description = description;
        this.tutor = tutor;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTutor() {
        return tutor;
    }

    public void setTutor(String tutor) {
        this.tutor = tutor;
    }

    public Set<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }
}
