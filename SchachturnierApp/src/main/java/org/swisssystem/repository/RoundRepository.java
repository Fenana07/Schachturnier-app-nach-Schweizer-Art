package org.swisssystem.repository;

import org.swisssystem.entity.Round;
import org.swisssystem.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoundRepository extends JpaRepository<Round, Long> {

    Optional<Round> findByTournamentAndNumber(Tournament tournament, Integer number);

    List<Round> findByTournamentOrderByNumberAsc(Tournament tournament);
}
