package org.swisssystem.service;

import org.swisssystem.entity.Participation;
import org.swisssystem.entity.Player;
import org.swisssystem.entity.Tournament;
import org.swisssystem.repository.ParticipationRepository;
import org.swisssystem.repository.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final ParticipationRepository participationRepository;

    public TournamentService(TournamentRepository tournamentRepository,
                             ParticipationRepository participationRepository) {
        this.tournamentRepository = tournamentRepository;
        this.participationRepository = participationRepository;
    }

    @Transactional
    public Tournament createTournament(String name,
                                       String location,
                                       int roundsCount,
                                       LocalDate start,
                                       LocalDate end) {
        Tournament t = new Tournament();
        t.setName(name);
        t.setLocation(location);
        t.setRoundsCount(roundsCount);
        t.setStartDate(start);
        t.setEndDate(end);
        t.setCreatedAt(Instant.now());
        return tournamentRepository.save(t);
    }

    @Transactional
    public Participation addPlayer(Tournament tournament,
                                   Player player,
                                   Integer seedNo,
                                   Integer rating,
                                   String title,
                                   String club) {
        Participation p = new Participation();
        p.setTournament(tournament);
        p.setPlayer(player);
        p.setSeedNo(seedNo);
        p.setInitialRating(rating);
        p.setTitle(title);
        p.setClub(club);
        return participationRepository.save(p);
    }
}
