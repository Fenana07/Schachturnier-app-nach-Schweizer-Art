package org.swisssystem.repository;

import org.swisssystem.entity.Participation;
import org.swisssystem.entity.Standing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StandingRepository extends JpaRepository<Standing, Long> {

    Optional<Standing> findByParticipation(Participation participation);

    List<Standing> findByParticipation_Tournament_IdOrderByPointsDesc(Long tournamentId);
}
