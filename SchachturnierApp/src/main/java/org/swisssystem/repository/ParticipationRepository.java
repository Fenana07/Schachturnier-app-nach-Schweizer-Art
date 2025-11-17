package org.swisssystem.repository;

import org.swisssystem.entity.Participation;
import org.swisssystem.entity.Player;
import org.swisssystem.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    Optional<Participation> findByTournamentAndPlayer(Tournament tournament, Player player);

    List<Participation> findByTournamentOrderBySeedNoAsc(Tournament tournament);
}
