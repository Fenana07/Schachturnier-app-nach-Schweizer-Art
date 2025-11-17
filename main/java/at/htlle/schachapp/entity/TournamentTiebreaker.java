package at.htlle.schachapp.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tournament_tiebreaker")
@IdClass(TournamentTiebreakerId.class)
public class TournamentTiebreaker {

    @Id
    @Column(name = "tournament_id", nullable = false)
    private Long tournamentId;

    @Id
    @Column(name = "order_no", nullable = false)
    private Integer orderNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", nullable = false, length = 30)
    private TieBreakerCode code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", insertable = false, updatable = false)
    private Tournament tournament;

    // --- Getter/Setter ---
    public Long getTournamentId() { return tournamentId; }
    public void setTournamentId(Long tournamentId) { this.tournamentId = tournamentId; }

    public Integer getOrderNo() { return orderNo; }
    public void setOrderNo(Integer orderNo) { this.orderNo = orderNo; }

    public TieBreakerCode getCode() { return code; }
    public void setCode(TieBreakerCode code) { this.code = code; }

    public Tournament getTournament() { return tournament; }
    public void setTournament(Tournament tournament) { this.tournament = tournament; }
}

/** ID-Klasse für den zusammengesetzten Schlüssel (tournament_id, order_no) */
class TournamentTiebreakerId implements Serializable {
    private Long tournamentId;
    private Integer orderNo;

    public TournamentTiebreakerId() {}
    public TournamentTiebreakerId(Long tournamentId, Integer orderNo) {
        this.tournamentId = tournamentId;
        this.orderNo = orderNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TournamentTiebreakerId that)) return false;
        return Objects.equals(tournamentId, that.tournamentId) &&
                Objects.equals(orderNo, that.orderNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tournamentId, orderNo);
    }
}