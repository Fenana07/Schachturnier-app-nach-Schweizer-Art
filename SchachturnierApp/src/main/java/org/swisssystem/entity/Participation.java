package org.swisssystem.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "participation",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_participation_tournament_player",
                        columnNames = {"tournament_id", "player_id"}
                ),
                @UniqueConstraint(
                        name = "uk_participation_tournament_seed",
                        columnNames = {"tournament_id", "seed_no"}
                )
        },
        indexes = {
                @Index(name = "idx_participation_player", columnList = "player_id")
        }
)
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "seed_no")
    private Integer seedNo;

    @Column(name = "initial_rating")
    private Integer initialRating;

    @Column(name = "title", length = 10)
    private String title;

    @Column(name = "club", length = 100)
    private String club;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Tournament getTournament() { return tournament; }
    public void setTournament(Tournament tournament) { this.tournament = tournament; }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }

    public Integer getSeedNo() { return seedNo; }
    public void setSeedNo(Integer seedNo) { this.seedNo = seedNo; }

    public Integer getInitialRating() { return initialRating; }
    public void setInitialRating(Integer initialRating) { this.initialRating = initialRating; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getClub() { return club; }
    public void setClub(String club) { this.club = club; }
}
