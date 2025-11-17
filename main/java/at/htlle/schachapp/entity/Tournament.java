package at.htlle.schachapp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tournament")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    private String location;
    private LocalDate startDate;
    private LocalDate endDate;

    @Column(nullable = false)
    private int roundsCount;

    private String timeControl;
    private Boolean allowByes;

    @Column(precision = 3, scale = 1)
    private Double byePoints;

    private LocalDateTime createdAt;

    // --- Beziehungen ---
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Round> rounds;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Match> matches;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Participation> participants;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Standing> standings;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<TournamentTiebreaker> tiebreakers;

    // Getter & Setter
    // (oder mit Lombok: @Getter @Setter @NoArgsConstructor @AllArgsConstructor)
}