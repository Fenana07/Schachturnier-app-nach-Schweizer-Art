package at.htlle.schachapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "match")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "round_id", nullable = false)
    private Round round;

    private Integer boardNo;

    @ManyToOne
    @JoinColumn(name = "white_player_id")
    private Player whitePlayer;

    @ManyToOne
    @JoinColumn(name = "black_player_id")
    private Player blackPlayer;

    private String resultCode;

    @Column(precision = 3, scale = 1)
    private Double resultPointsWhite;

    @Column(precision = 3, scale = 1)
    private Double resultPointsBlack;

    private Boolean isForfeit;
    private String notes;
}