package com.lolmyeon.lol.party.entiry;

import com.lolmyeon.lol.common.LolLine;
import com.lolmyeon.lol.member.entity.Member;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "parties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 파티 만든 사람
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Member creator;

    /**
     * 파티 종류
     * NORMAL = 내전
     * FREE   = 자유내전
     * FLEX   = 자유랭크
     * ARAM   = 증바람
     */
    @Enumerated(EnumType.STRING)
    private ParticipationType participationType;

    /**
     * 파티 시간
     * 예: "20", "21", "22", "23:30"
     */
    private String partyTime;

    /**
     * 파티장이 선택한 라인
     *
     * NORMAL / FREE / FLEX 는 필요
     * ARAM 은 라인 없음이라 null 가능
     */
    @Enumerated(EnumType.STRING)
    private LolLine creatorLine;

    /**
     * 파티 제목
     */
    private String title;

    /**
     * 파티 설명 / 메모
     *
     * 일단 기존 코드 때문에 남겨둠.
     * 나중에 화면에서 안 쓰면 나중에 제거 가능.
     */
    private String memo;

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