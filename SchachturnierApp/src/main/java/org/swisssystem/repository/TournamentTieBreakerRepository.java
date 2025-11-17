package org.swisssystem.repository;

import org.swisssystem.entity.Tournament;
import org.swisssystem.entity.TournamentTieBreaker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TournamentTieBreakerRepository extends JpaRepository<TournamentTieBreaker, Long> {

    List<TournamentTieBreaker> findByTournamentOrderByOrderNoAsc(Tournament tournament);
}
