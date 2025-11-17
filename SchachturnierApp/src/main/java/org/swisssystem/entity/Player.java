package org.swisssystem.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "player",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_player_fide_id", columnNames = "fide_id")
        }
)
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "fide_id", length = 20)
    private String fideId;

    @Column(name = "national_id", length = 20)
    private String nationalId;

    @Column(name = "initial_rating")
    private Integer initialRating;

    @Column(name = "created_at")
    private Instant createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFideId() { return fideId; }
    public void setFideId(String fideId) { this.fideId = fideId; }

    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }

    public Integer getInitialRating() { return initialRating; }
    public void setInitialRating(Integer initialRating) { this.initialRating = initialRating; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
