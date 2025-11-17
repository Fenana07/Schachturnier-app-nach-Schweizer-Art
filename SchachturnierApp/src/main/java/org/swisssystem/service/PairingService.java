package org.swisssystem.service;

import org.swisssystem.entity.Match;
import org.swisssystem.entity.Participation;
import org.swisssystem.entity.Round;
import org.swisssystem.entity.Standing;
import org.swisssystem.entity.Tournament;
import org.swisssystem.entity.enums.ResultCode;
import org.swisssystem.repository.MatchRepository;
import org.swisssystem.repository.ParticipationRepository;
import org.swisssystem.repository.StandingRepository;
import org.swisssystem.util.PairingUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PairingService {

    private final ParticipationRepository participationRepository;
    private final StandingRepository standingRepository;
    private final MatchRepository matchRepository;

    public PairingService(ParticipationRepository participationRepository,
                          StandingRepository standingRepository,
                          MatchRepository matchRepository) {
        this.participationRepository = participationRepository;
        this.standingRepository = standingRepository;
        this.matchRepository = matchRepository;
    }

    /**
     * Einfache Schweizer-Paarung:
     * - Teilnehmer nach Punkten (Standing) sortieren, dann nach Startnummer
     * - von oben nach unten paaren
     * - falls ungerade: BYE am Schluss
     */
    @Transactional
    public void generatePairingsForRound(Tournament tournament, Round round) {
        // 1. Alle Teilnehmer des Turniers holen
        List<Participation> participations =
                participationRepository.findByTournamentOrderBySeedNoAsc(tournament);

        if (participations.isEmpty()) {
            System.out.println("Keine Teilnehmer für Turnier: " + tournament.getName());
            return;
        }

        // 2. Punkte aus Standing holen (falls kein Standing: 0.0)
        Map<Long, Double> scoreMap = new HashMap<>();
        for (Participation p : participations) {
            Standing s = standingRepository.findByParticipation(p).orElse(null);
            double pts = (s == null || s.getPoints() == null)
                    ? 0.0
                    : s.getPoints().doubleValue();
            scoreMap.put(p.getId(), pts);
        }

        // 3. Sortieren: zuerst Punkte (desc), dann Startnummer (asc)
        participations.sort((a, b) -> {
            double pa = scoreMap.getOrDefault(a.getId(), 0.0);
            double pb = scoreMap.getOrDefault(b.getId(), 0.0);

            int cmp = Double.compare(pb, pa); // mehr Punkte zuerst
            if (cmp != 0) {
                return cmp;
            }

            Integer sa = a.getSeedNo() == null ? Integer.MAX_VALUE : a.getSeedNo();
            Integer sb = b.getSeedNo() == null ? Integer.MAX_VALUE : b.getSeedNo();
            return Integer.compare(sa, sb); // kleinere Startnummer zuerst
        });

        // 4. Kopie der Liste als Arbeits-Pool
        List<Participation> pool = new ArrayList<>(participations);
        int boardNo = 1;

        // 5. Normale Paarungen
        while (pool.size() >= 2) {
            Participation p1 = pool.remove(0);
            Participation p2 = pool.remove(0);

            Match m = new Match();
            m.setRound(round);
            m.setBoardNo(boardNo++);

            // aktuell simpel: p1 = Weiß, p2 = Schwarz
            m.setWhiteParticipation(p1);
            m.setBlackParticipation(p2);

            // Normalisiertes Paar für Rematch-Sperre
            PairingUtils.setNormalizedPair(m, p1, p2);

            // WICHTIG: Initialer ResultCode, damit NOT NULL erfüllt ist
            m.setResultCode(ResultCode.ZERO_ZERO);

            matchRepository.save(m);
        }

        // 6. BYE, falls einer übrig bleibt
        if (!pool.isEmpty()) {
            Participation byePlayer = pool.remove(0);

            Match m = new Match();
            m.setRound(round);
            m.setBoardNo(boardNo);

            // Spieler bekommt ein BYE, andere Seite bleibt null
            m.setWhiteParticipation(byePlayer);
            m.setBlackParticipation(null);
            m.setPairLowParticipation(null);
            m.setPairHighParticipation(null);
            m.setResultCode(ResultCode.BYE);

            matchRepository.save(m);
        }
    }
}
