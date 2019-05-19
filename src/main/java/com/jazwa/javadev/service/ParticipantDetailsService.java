package com.jazwa.javadev.service;

import com.jazwa.javadev.model.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.EntityNotFoundException;

public class ParticipantDetailsService implements UserDetailsService {

    @Autowired
    ParticipantService participantService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Participant participant = participantService.getByEmail(s).orElseThrow(EntityNotFoundException::new);
        return new ParticipantDetails(participant);
    }
}
