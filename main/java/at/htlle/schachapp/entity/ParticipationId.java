package at.htlle.schachapp.entity;

import java.io.Serializable;
import java.util.Objects;

public class ParticipationId implements Serializable {
    private Long tournamentId;
    private Long playerId;

    // equals & hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParticipationId)) return false;
        ParticipationId that = (ParticipationId) o;
        return Objects.equals(tournamentId, that.tournamentId) &&
                Objects.equals(playerId, that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tournamentId, playerId);
    }
}