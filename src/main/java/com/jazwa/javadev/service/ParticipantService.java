package com.jazwa.javadev.service;

import com.jazwa.javadev.model.Participant;

import java.util.Optional;
import java.util.Set;

public interface ParticipantService {

    Set<Participant> findAll();

    Optional<Participant> findByIndex(Integer index);

    Optional<Participant> findByEmail(String email);

    boolean deleteByIndex(Integer index);

    boolean deleteByEmail(String email);

    boolean addWithIndex(Integer index,String password);

    boolean addWithEmail(String email, String password);
}
