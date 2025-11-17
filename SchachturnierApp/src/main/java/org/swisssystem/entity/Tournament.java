package org.swisssystem.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "tournament")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "rounds_count", nullable = false)
    private Integer roundsCount;

    @Column(name = "time_control", length = 50)
    private String timeControl;

    @Column(name = "allow_byes")
    private Boolean allowByes = Boolean.TRUE;

    @Column(name = "bye_points", precision = 3, scale = 1)
    private BigDecimal byePoints = new BigDecimal("0.5");

    @Column(name = "created_at")
    private Instant createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Integer getRoundsCount() { return roundsCount; }
    public void setRoundsCount(Integer roundsCount) { this.roundsCount = roundsCount; }

    public String getTimeControl() { return timeControl; }
    public void setTimeControl(String timeControl) { this.timeControl = timeControl; }

    public Boolean getAllowByes() { return allowByes; }
    public void setAllowByes(Boolean allowByes) { this.allowByes = allowByes; }

    public BigDecimal getByePoints() { return byePoints; }
    public void setByePoints(BigDecimal byePoints) { this.byePoints = byePoints; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
