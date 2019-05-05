package com.jazwa.javadev.domain;

import com.jazwa.javadev.model.Participant;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class ParticipantDetails implements UserDetails {
    private Participant participant;

    public ParticipantDetails(Participant participant) {
        this.participant = participant;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(participant.getRole());
    }

    @Override
    public String getPassword() {
        return participant.getPassword();
    }

    @Override
    public String getUsername() {
        return participant.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
