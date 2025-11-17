package org.swisssystem.entity;

import jakarta.persistence.*;
import org.swisssystem.entity.enums.ResultCode;

@Entity
@Table(
        name = "match",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_match_round_board",
                        columnNames = {"round_id", "board_no"}
                ),
                @UniqueConstraint(
                        name = "uk_match_pair_low_high",
                        columnNames = {"pair_low_participation_id", "pair_high_participation_id"}
                )
        },
        indexes = {
                @Index(name = "idx_match_round_white", columnList = "round_id, white_participation_id"),
                @Index(name = "idx_match_round_black", columnList = "round_id, black_participation_id")
        }
)
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "round_id", nullable = false)
    private Round round;

    @Column(name = "board_no", nullable = false)
    private Integer boardNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "white_participation_id")
    private Participation whiteParticipation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "black_participation_id")
    private Participation blackParticipation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pair_low_participation_id")
    private Participation pairLowParticipation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pair_high_participation_id")
    private Participation pairHighParticipation;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_code", nullable = false, length = 20)
    private ResultCode resultCode;

    @Column(name = "notes", length = 500)
    private String notes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Round getRound() { return round; }
    public void setRound(Round round) { this.round = round; }

    public Integer getBoardNo() { return boardNo; }
    public void setBoardNo(Integer boardNo) { this.boardNo = boardNo; }

    public Participation getWhiteParticipation() { return whiteParticipation; }
    public void setWhiteParticipation(Participation whiteParticipation) { this.whiteParticipation = whiteParticipation; }

    public Participation getBlackParticipation() { return blackParticipation; }
    public void setBlackParticipation(Participation blackParticipation) { this.blackParticipation = blackParticipation; }

    public Participation getPairLowParticipation() { return pairLowParticipation; }
    public void setPairLowParticipation(Participation pairLowParticipation) { this.pairLowParticipation = pairLowParticipation; }

    public Participation getPairHighParticipation() { return pairHighParticipation; }
    public void setPairHighParticipation(Participation pairHighParticipation) { this.pairHighParticipation = pairHighParticipation; }

    public ResultCode getResultCode() { return resultCode; }
    public void setResultCode(ResultCode resultCode) { this.resultCode = resultCode; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
