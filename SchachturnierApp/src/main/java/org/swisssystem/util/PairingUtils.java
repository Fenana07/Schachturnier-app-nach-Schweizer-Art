package org.swisssystem.util;

import org.swisssystem.entity.Match;
import org.swisssystem.entity.Participation;

public class PairingUtils {

    private PairingUtils() {
    }

    /**
     * Setzt pairLow / pairHigh für eine Partie, um Rematches zu verhindern.
     * Bei BYE (eine Seite null) sollst du diese Methode NICHT aufrufen.
     */
    public static void setNormalizedPair(Match match, Participation white, Participation black) {
        if (white == null || black == null) {
            match.setPairLowParticipation(null);
            match.setPairHighParticipation(null);
            return;
        }

        if (white.getId() < black.getId()) {
            match.setPairLowParticipation(white);
            match.setPairHighParticipation(black);
        } else {
            match.setPairLowParticipation(black);
            match.setPairHighParticipation(white);
        }
    }
}
