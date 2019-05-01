package com.jazwa.javadev.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Admin {
    @Id
    private String login;
    private String password;
}
