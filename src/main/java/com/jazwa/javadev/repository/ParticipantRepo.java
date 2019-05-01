package com.jazwa.javadev.repository;

import com.jazwa.javadev.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepo extends JpaRepository<Participant,Integer> {
}
