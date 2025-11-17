package at.htlle.schachapp.entity;

import at.htlle.schachapp.entity.Match;
import at.htlle.schachapp.entity.Tournament;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "round")
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @Column(nullable = false)
    private int number;

    private LocalDateTime startTime;
    private Boolean published;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL)
    private List<Match> matches;
}