package org.swisssystem.service;

import org.swisssystem.entity.Match;
import org.swisssystem.entity.Participation;
import org.swisssystem.entity.Standing;
import org.swisssystem.entity.Tournament;
import org.swisssystem.entity.enums.ResultCode;
import org.swisssystem.repository.MatchRepository;
import org.swisssystem.repository.ParticipationRepository;
import org.swisssystem.repository.StandingRepository;
import org.swisssystem.util.ResultUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class StandingService {

    private final StandingRepository standingRepository;
    private final MatchRepository matchRepository;
    private final ParticipationRepository participationRepository;

    public StandingService(StandingRepository standingRepository,
                           MatchRepository matchRepository,
                           ParticipationRepository participationRepository) {
        this.standingRepository = standingRepository;
        this.matchRepository = matchRepository;
        this.participationRepository = participationRepository;
    }

    @Transactional
    public void recalculateForTournament(Tournament tournament) {
        List<Participation> participations =
                participationRepository.findByTournamentOrderBySeedNoAsc(tournament);

        for (Participation p : participations) {
            Standing standing = standingRepository.findByParticipation(p)
                    .orElseGet(() -> {
                        Standing s = new Standing();
                        s.setParticipation(p);
                        return s;
                    });

            BigDecimal points = BigDecimal.ZERO;
            int wins = 0;
            int blacks = 0;

            List<Match> matches =
                    matchRepository.findByWhiteParticipationOrBlackParticipation(p, p);

            for (Match m : matches) {
                ResultCode rc = m.getResultCode();

                // normale Punkte
                points = points.add(ResultUtils.getPointsForMatch(p, m));

                // BYE: addiere byePoints des Turniers
                if (rc == ResultCode.BYE) {
                    points = points.add(tournament.getByePoints());
                }

                if (ResultUtils.isWinFor(p, m)) {
                    wins++;
                }

                if (p.equals(m.getBlackParticipation())) {
                    blacks++;
                }
            }

            standing.setPoints(points);
            standing.setWins(wins);
            standing.setBlacks(blacks);
            standing.setLastUpdated(Instant.now());

            // Buchholz, Median-Buchholz, Sonneborn-Berger:
            // kannst du später in zweitem Schritt berechnen.
            standingRepository.save(standing);
        }
    }
}
