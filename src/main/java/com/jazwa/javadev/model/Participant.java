package com.jazwa.javadev.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Participant {
    @Id
    private Integer index;
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    private String surname;
    private Integer yearOfStudy;
    private String fieldOfStudy;
    private Role role;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = {@JoinColumn(name = "participant_index")},
    inverseJoinColumns = {@JoinColumn(name = "courseclass_starttime")})
    private Set<CourseClass> classes;

    public Participant() {
    }

    public Participant(Integer index, String email, String password, String name, String surname, Integer yearOfStudy, String fieldOfStudy, Role role) {
        this.index = index;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.yearOfStudy = yearOfStudy;
        this.fieldOfStudy = fieldOfStudy;
        this.role = role;
    }

    public void changePassword(String oldPassword, String newPassword, String repeatNewPassword){

    }

    public void addClass(CourseClass courseClass){

    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(Integer yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
