package com.jazwa.javadev.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class CourseClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private LocalDateTime startTime;
    private String title;
    private String description;
    private String tutor;
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Participant> participants = new HashSet<>();

    public void addParticipant(Participant participant){
        this.participants.add(participant);
    }

    public void removeParticipant(Participant participant){
        this.participants.remove(participant);
    }

    public void addParticipants(List<Participant> participants){
        this.participants.addAll(participants);
    }

    public Set<Participant> getParticipants() {
        return participants;
    }

    public CourseClass() {
    }

    public CourseClass(LocalDateTime startTime, String title, String description, String tutor) {
        this.startTime = startTime;
        this.title = title;
        this.description = description;
        this.tutor = tutor;
    }
    public Long getId() {
        return id;
    }
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSS")
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

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseClass that = (CourseClass) o;
        return id.equals(that.id) &&
                startTime.equals(that.startTime) &&
                title.equals(that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(tutor, that.tutor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, title, description, tutor);
    }
}
