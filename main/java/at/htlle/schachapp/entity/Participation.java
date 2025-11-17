package at.htlle.schachapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "participation")
@IdClass(ParticipationId.class)
public class Participation {

    @Id
    private Long tournamentId;

    @Id
    private Long playerId;

    private Integer seedNo;
    private Integer initialRating;
    private String title;
    private String club;

    @ManyToOne
    @JoinColumn(name = "tournament_id", insertable = false, updatable = false)
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "player_id", insertable = false, updatable = false)
    private Player player;
}