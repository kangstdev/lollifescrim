package com.lolmyeon.lol.waiting.entity;

import com.lolmyeon.lol.member.entity.Member;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WaitingParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 대기 신청한 회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 내전 / 자유내전 / 자유랭크 / 증바람
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipationType participationType;

    // 20, 21, 22 같은 시간
    @Column(nullable = false)
    private String selectedTime;

    // 희망 라인: TOP, JUNGLE, MID, ADC, SUP, ANY
    @Column(nullable = false)
    private String selectedLine;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}