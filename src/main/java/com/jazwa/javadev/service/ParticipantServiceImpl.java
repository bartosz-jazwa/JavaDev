package com.jazwa.javadev.service;

import com.jazwa.javadev.model.Participant;
import com.jazwa.javadev.repository.ParticipantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ParticipantServiceImpl implements ParticipantService {
    @Autowired
    ParticipantRepo participantRepo;

    @Override
    public Set<Participant> findAll() {
        return new HashSet<>(participantRepo.findAll());
    }

    @Override
    public Optional<Participant> findByIndex(Integer index) {
        return participantRepo.findById(index);
    }

    @Override
    public Optional<Participant> findByEmail(String email) {
        return Optional.ofNullable(participantRepo.getParticipantByEmail(email));
    }

    @Override
    public boolean deleteByIndex(Integer index) {
        participantRepo.deleteById(index);
        return true;
    }

    @Override
    public boolean deleteByEmail(String email) {
        Participant p = participantRepo.getParticipantByEmail(email);
        participantRepo.delete(p);
        return true;
    }

    @Override
    public boolean addWithIndex(Integer index, String password) {
        Participant p = new Participant(index,"",password,"","",null,null);
        participantRepo.save(p);
        return true;
    }

    @Override
    public boolean addWithEmail(String email, String password) {
        Participant p = new Participant(null,email,password,"","",null,null);
        return false;
    }
}
