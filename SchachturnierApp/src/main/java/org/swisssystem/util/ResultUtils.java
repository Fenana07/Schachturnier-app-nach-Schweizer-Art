package org.swisssystem.util;

import org.swisssystem.entity.Match;
import org.swisssystem.entity.Participation;
import org.swisssystem.entity.enums.ResultCode;

import java.math.BigDecimal;

public class ResultUtils {

    private ResultUtils() {
    }

    /**
     * Punkte, die eine Participation aus einer einzelnen Partie bekommt.
     * BYE wird hier NICHT behandelt (weil dafür Tournament.byePoints nötig ist).
     */
    public static BigDecimal getPointsForMatch(Participation player, Match match) {
        ResultCode rc = match.getResultCode();
        if (rc == null) {
            return BigDecimal.ZERO;
        }

        boolean isWhite = player.equals(match.getWhiteParticipation());
        boolean isBlack = player.equals(match.getBlackParticipation());

        if (!isWhite && !isBlack) {
            return BigDecimal.ZERO;
        }

        return switch (rc) {
            case ONE_ZERO -> isWhite ? BigDecimal.ONE : BigDecimal.ZERO;
            case ZERO_ONE -> isBlack ? BigDecimal.ONE : BigDecimal.ZERO;
            case HALF_HALF -> new BigDecimal("0.5");
            case ZERO_ZERO -> BigDecimal.ZERO;
            case F1_0 -> isWhite ? BigDecimal.ONE : BigDecimal.ZERO;
            case ZERO_F1 -> isBlack ? BigDecimal.ONE : BigDecimal.ZERO;
            case BYE -> BigDecimal.ZERO; // richtiger Wert kommt aus Tournament.byePoints
        };
    }

    public static boolean isWinFor(Participation player, Match match) {
        ResultCode rc = match.getResultCode();
        if (rc == null) {
            return false;
        }

        boolean isWhite = player.equals(match.getWhiteParticipation());
        boolean isBlack = player.equals(match.getBlackParticipation());

        return switch (rc) {
            case ONE_ZERO, F1_0 -> isWhite;
            case ZERO_ONE, ZERO_F1 -> isBlack;
            default -> false;
        };
    }
}
