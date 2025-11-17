package org.swisssystem.entity;

import jakarta.persistence.*;
import org.swisssystem.entity.enums.TieBreakerType;

@Entity
@Table(
        name = "tournament_tiebreaker",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tournament_tiebreaker_order",
                        columnNames = {"tournament_id", "order_no"}
                ),
                @UniqueConstraint(
                        name = "uk_tournament_tiebreaker_type",
                        columnNames = {"tournament_id", "type"}
                )
        }
)
public class TournamentTieBreaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @Column(name = "order_no", nullable = false)
    private Integer orderNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private TieBreakerType type;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Tournament getTournament() { return tournament; }
    public void setTournament(Tournament tournament) { this.tournament = tournament; }

    public Integer getOrderNo() { return orderNo; }
    public void setOrderNo(Integer orderNo) { this.orderNo = orderNo; }

    public TieBreakerType getType() { return type; }
    public void setType(TieBreakerType type) { this.type = type; }
}
