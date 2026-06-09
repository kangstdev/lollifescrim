package com.lolmyeon.lol.participation.entity;

import com.lolmyeon.lol.common.LolLine;
import com.lolmyeon.lol.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "participations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 신청한 회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 참여 종류: 내전, 자유내전, 자유랭크, 증바람
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ParticipationType participationType;

    // 선택한 시간들
    // 예: "20,21" 또는 "오후 8시,오후 9시"
    @Column(nullable = false, length = 100)
    private String selectedTimes;

    // 선택한 라인
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LolLine selectedLine;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}