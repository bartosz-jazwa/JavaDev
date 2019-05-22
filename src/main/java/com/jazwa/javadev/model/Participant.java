package com.jazwa.javadev.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Participant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true)
    private Integer index;
    @Column(unique = true,nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String name;
    private String surname;
    private Integer yearOfStudy;
    private String fieldOfStudy;
    private Role role;
    @ManyToMany(mappedBy = "participants")
    private Set<CourseClass> classes;

    public Participant() {
    }

    public Participant(String email, String password, String name, String surname, Integer yearOfStudy, String fieldOfStudy, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.yearOfStudy = yearOfStudy;
        this.fieldOfStudy = fieldOfStudy;
        this.role = role;
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

    public Long getId() {
        return id;
    }

    public Set<CourseClass> getClasses() {
        return classes;
    }

    public void setClasses(Set<CourseClass> classes) {
        this.classes = classes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return id.equals(that.id) &&
                Objects.equals(index, that.index) &&
                email.equals(that.email) &&
                Objects.equals(password, that.password) &&
                Objects.equals(name, that.name) &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(yearOfStudy, that.yearOfStudy) &&
                Objects.equals(fieldOfStudy, that.fieldOfStudy) &&
                role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, index, email, password, name, surname, yearOfStudy, fieldOfStudy, role);
    }
}
