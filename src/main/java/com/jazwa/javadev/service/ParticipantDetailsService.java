package com.jazwa.javadev.service;

import com.jazwa.javadev.model.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;

@Service(value = "userDetail")
public class ParticipantDetailsService implements UserDetailsService {

    @Autowired
    ParticipantService participantService;

    @Override
    public UserDetails loadUserByUsername(String s) {
        Participant participant = participantService.getByEmail(s).orElseThrow(() -> new UsernameNotFoundException(s));
        /*return User.withUsername(participant.getEmail())
                .password(participant.getPassword())
                .disabled(false)
                .accountExpired(false)
                .authorities(participant.getRole().toString())
                .build();*/
        return new ParticipantDetails(participant);
    }
}
