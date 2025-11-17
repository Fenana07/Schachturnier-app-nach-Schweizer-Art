package org.swisssystem.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "round",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_round_tournament_number",
                        columnNames = {"tournament_id", "number"}
                )
        }
)
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "published")
    private Boolean published = Boolean.FALSE;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Tournament getTournament() { return tournament; }
    public void setTournament(Tournament tournament) { this.tournament = tournament; }

    public Integer getNumber() { return number; }
    public void setNumber(Integer number) { this.number = number; }

    public Instant getStartTime() { return startTime; }
    public void setStartTime(Instant startTime) { this.startTime = startTime; }

    public Boolean getPublished() { return published; }
    public void setPublished(Boolean published) { this.published = published; }
}
