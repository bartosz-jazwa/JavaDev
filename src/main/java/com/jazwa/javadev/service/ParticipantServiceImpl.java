package com.jazwa.javadev.service;

import com.jazwa.javadev.model.Participant;
import com.jazwa.javadev.model.Role;
import com.jazwa.javadev.repository.ParticipantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
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
    public Set<Participant> getAll() {
        Set<Participant> participants = new HashSet<>();
        participants.addAll(participantRepo.findAll());
        return participants;
    }

    @Override
    public Optional<Participant> getByIndex(Integer index) {
        return participantRepo.getParticipantByIndex(index);
    }

    @Override
    public Optional<Participant> getByEmail(String email) {
        return participantRepo.getParticipantByEmail(email);
    }

    @Override
    public Optional<Participant> getById(Long id) {
        return participantRepo.findById(id);
    }

    @Override
    public Set<Participant> getByRole(Role role) {

        Set<Participant> participants = new HashSet<>();
        participants.addAll(participantRepo.getParticipantByRole(role));
        return participants;
    }

    @Override
    public Optional<Participant> deleteByIndex(Integer index) {
        return Optional.empty();
    }

    @Override
    public Optional<Participant> deleteByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<Participant> deleteById(Long id) {
        Optional<Participant> result = participantRepo.findById(id);
        result.ifPresent(participant -> participantRepo.delete(participant));
        return result;
    }

    @Override
    public Optional<Participant> addWithIndex(Integer index, String password) {
        return Optional.empty();
    }

    @Override
    public Optional<Participant> addWithEmail(String email, String password) {
        return Optional.empty();
    }

    @Override
    public Optional<Participant> addNew(Participant participant) {
        Optional<Participant> result;
        try {
            result = Optional.ofNullable(participantRepo.save(participant));
        } catch (DataIntegrityViolationException e){
            return Optional.empty();
        }
        return result;
    }

    @Override
    public Optional<Participant> save(Participant participant) {
        return Optional.ofNullable(participantRepo.save(participant));
    }
}
