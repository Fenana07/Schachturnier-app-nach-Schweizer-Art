package org.swisssystem.service;

import org.swisssystem.entity.Match;
import org.swisssystem.entity.Participation;
import org.swisssystem.entity.Round;
import org.swisssystem.entity.enums.ResultCode;
import org.swisssystem.repository.MatchRepository;
import org.swisssystem.util.PairingUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Transactional
    public Match createMatch(Round round,
                             int boardNo,
                             Participation white,
                             Participation black) {
        Match m = new Match();
        m.setRound(round);
        m.setBoardNo(boardNo);
        m.setWhiteParticipation(white);
        m.setBlackParticipation(black);
        PairingUtils.setNormalizedPair(m, white, black);
        m.setResultCode(ResultCode.ZERO_ZERO); // initial: keine Wertung
        return matchRepository.save(m);
    }

    @Transactional
    public Match createBye(Round round,
                           int boardNo,
                           Participation player) {
        Match m = new Match();
        m.setRound(round);
        m.setBoardNo(boardNo);
        // z. B. immer als "white"
        m.setWhiteParticipation(player);
        m.setBlackParticipation(null);
        m.setPairLowParticipation(null);
        m.setPairHighParticipation(null);
        m.setResultCode(ResultCode.BYE);
        return matchRepository.save(m);
    }

    @Transactional
    public void setResult(Match match, ResultCode resultCode, String notes) {
        match.setResultCode(resultCode);
        match.setNotes(notes);
        matchRepository.save(match);
    }
}
