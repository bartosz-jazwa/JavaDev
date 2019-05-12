package com.jazwa.javadev.service;

import com.jazwa.javadev.domain.ParticipantDetails;
import com.jazwa.javadev.model.Participant;
import com.jazwa.javadev.repository.ParticipantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service("participantDetailsService")
public class ParticipantDetailsService implements UserDetailsService {
    @Autowired
    private ParticipantRepo participantRepo;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Participant participant = participantRepo.getParticipantByEmail(s);
        if(participant == null){
            throw  new UsernameNotFoundException("Participant not found");
        }

        UserDetails userDetails =new User(participant.getEmail(),encoder.encode(participant.getPassword()), Arrays.asList(participant.getRole()));
        return userDetails;
    }
}