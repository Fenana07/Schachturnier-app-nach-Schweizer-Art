package at.htlle.schachapp.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    private String fideId;
    private String nationalId;
    private Integer initialRating;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "whitePlayer")
    private List<Match> whiteMatches;

    @OneToMany(mappedBy = "blackPlayer")
    private List<Match> blackMatches;

    @OneToMany(mappedBy = "player")
    private List<Participation> participations;

    @OneToMany(mappedBy = "player")
    private List<Standing> standings;
}
