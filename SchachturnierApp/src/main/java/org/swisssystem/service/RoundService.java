package org.swisssystem.service;

import org.swisssystem.entity.Round;
import org.swisssystem.entity.Tournament;
import org.swisssystem.repository.RoundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class RoundService {

    private final RoundRepository roundRepository;

    public RoundService(RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    @Transactional
    public Round createRound(Tournament tournament, int roundNumber, Instant startTime) {
        Round r = new Round();
        r.setTournament(tournament);
        r.setNumber(roundNumber);
        r.setStartTime(startTime);
        r.setPublished(false);
        return roundRepository.save(r);
    }

    @Transactional
    public void publishRound(Round round) {
        round.setPublished(true);
        roundRepository.save(round);
    }
}
