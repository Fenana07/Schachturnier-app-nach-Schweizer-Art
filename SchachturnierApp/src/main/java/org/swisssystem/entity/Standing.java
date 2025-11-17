package org.swisssystem.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "standing",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_standing_participation",
                        columnNames = {"participation_id"}
                )
        }
)
public class Standing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participation_id", nullable = false)
    private Participation participation;

    @Column(name = "points", precision = 4, scale = 2, nullable = false)
    private BigDecimal points;

    @Column(name = "buchholz", precision = 5, scale = 2)
    private BigDecimal buchholz;

    @Column(name = "median_buchholz", precision = 5, scale = 2)
    private BigDecimal medianBuchholz;

    @Column(name = "sonneborn_berger", precision = 6, scale = 2)
    private BigDecimal sonnebornBerger;

    @Column(name = "wins")
    private Integer wins;

    @Column(name = "blacks")
    private Integer blacks;

    @Column(name = "head_to_head", precision = 4, scale = 2)
    private BigDecimal headToHead;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Participation getParticipation() { return participation; }
    public void setParticipation(Participation participation) { this.participation = participation; }

    public BigDecimal getPoints() { return points; }
    public void setPoints(BigDecimal points) { this.points = points; }

    public BigDecimal getBuchholz() { return buchholz; }
    public void setBuchholz(BigDecimal buchholz) { this.buchholz = buchholz; }

    public BigDecimal getMedianBuchholz() { return medianBuchholz; }
    public void setMedianBuchholz(BigDecimal medianBuchholz) { this.medianBuchholz = medianBuchholz; }

    public BigDecimal getSonnebornBerger() { return sonnebornBerger; }
    public void setSonnebornBerger(BigDecimal sonnebornBerger) { this.sonnebornBerger = sonnebornBerger; }

    public Integer getWins() { return wins; }
    public void setWins(Integer wins) { this.wins = wins; }

    public Integer getBlacks() { return blacks; }
    public void setBlacks(Integer blacks) { this.blacks = blacks; }

    public BigDecimal getHeadToHead() { return headToHead; }
    public void setHeadToHead(BigDecimal headToHead) { this.headToHead = headToHead; }

    public Instant getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Instant lastUpdated) { this.lastUpdated = lastUpdated; }
}
