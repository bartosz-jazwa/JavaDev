package com.jazwa.javadev.repository;

import com.jazwa.javadev.model.Participant;
import com.jazwa.javadev.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ParticipantRepo extends JpaRepository<Participant,Long> {

    Optional<Participant> getParticipantByEmail(String email);

    Optional<Participant> getParticipantByIndex(Integer index);

    Set<Participant> getParticipantByRole(Role role);

}
