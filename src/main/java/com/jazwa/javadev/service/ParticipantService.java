package com.jazwa.javadev.service;

import com.jazwa.javadev.model.Participant;
import com.jazwa.javadev.model.Role;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;


public interface ParticipantService {

    Set<Participant> getAll();

    Optional<Participant> getByIndex(Integer index);

    Optional<Participant> getByEmail(String email);

    Optional<Participant> getById(Long id);

    Set<Participant> getByRole(Role role);

    Optional<Participant> deleteByIndex(Integer index);

    Optional<Participant> deleteByEmail(String email);

    Optional<Participant> deleteById(Long id);

    Optional<Participant> addWithIndex(Integer index,String password);

    Optional<Participant> addWithEmail(String email, String password);
}
