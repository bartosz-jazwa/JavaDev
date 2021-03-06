package com.jazwa.javadev.service;

import com.jazwa.javadev.model.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ParticipantDetailsService implements UserDetailsService {

    @Autowired
    ParticipantService participantService;

    @Override
    public UserDetails loadUserByUsername(String s) {


        Participant participant;
        Integer index;
        try {
            index = Integer.parseInt(s);
            participant = participantService.getByIndex(index)
                    .orElseThrow(() -> new UsernameNotFoundException(s));
        } catch (NumberFormatException e) {
            if (Participant.isValidEmailAddress(s)) {
                participant = participantService.getByEmail(s)
                        .orElseThrow(() -> new UsernameNotFoundException(s));
            }else {
                participant = new Participant();
            }
        }
        return new ParticipantDetails(participant);
    }
}