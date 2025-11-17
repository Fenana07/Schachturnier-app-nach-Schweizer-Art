package org.swisssystem.repository;

import org.swisssystem.entity.Match;
import org.swisssystem.entity.Participation;
import org.swisssystem.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByRound(Round round);

    List<Match> findByWhiteParticipationOrBlackParticipation(Participation white, Participation black);
}
