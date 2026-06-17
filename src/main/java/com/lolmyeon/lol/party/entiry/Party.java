package com.lolmyeon.lol.party.entity;

import com.lolmyeon.lol.member.entity.Member;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 파티 엔티티
 *
 * 역할:
 * 사용자가 직접 만든 파티 정보를 저장합니다.
 *
 * 예:
 * - 19시 자유내전 사람 구합니다.
 * - 23시 자유랭크 파티 구합니다.
 * - 20시 증바람 할 사람 모집합니다.
 *
 * 기존 Participation은 "참여 신청"이고,
 * Party는 "파티 생성"입니다.
 *
 * Participation:
 * - 내가 어떤 시간에 어떤 라인으로 참여 신청했는지
 *
 * Party:
 * - 누군가가 만든 파티방 정보
 */
@Entity
@Table(name = "parties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Party {

    /**
     * 파티 고유 번호
     *
     * MySQL AUTO_INCREMENT 방식으로 자동 증가합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 파티를 만든 회원
     *
     * 예:
     * test님이 19시 자유내전 파티를 만들었다면
     * creator에는 test 회원 정보가 들어갑니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Member creator;

    /**
     * 파티 종류
     *
     * 기존 ParticipationType을 그대로 재사용합니다.
     *
     * NORMAL = 내전
     * FREE   = 자유내전
     * FLEX   = 자유랭크
     * ARAM   = 증바람
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "participation_type", nullable = false, length = 20)
    private ParticipationType participationType;

    /**
     * 파티 시간
     *
     * 예:
     * "19"
     * "20"
     * "21"
     * "22"
     *
     * 일단은 날짜 없이 시간만 저장합니다.
     * 나중에 날짜별 파티까지 만들고 싶으면 partyDate 필드를 추가하면 됩니다.
     */
    @Column(name = "party_time", nullable = false, length = 10)
    private String partyTime;

    /**
     * 파티 제목
     *
     * 예:
     * "19시 자유내전 구합니다"
     * "23시 자유랭크 하실 분"
     */
    @Column(nullable = false, length = 100)
    private String title;

    /**
     * 파티 설명 / 메모
     *
     * 예:
     * "즐겜 위주로 갑니다."
     * "마이크 가능하신 분이면 좋습니다."
     */
    @Column(length = 500)
    private String memo;

    /**
     * 파티 생성 시간
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * 파티 수정 시간
     */
    private LocalDateTime updatedAt;

    /**
     * 처음 저장될 때 자동 실행됩니다.
     *
     * createdAt, updatedAt을 현재 시간으로 넣습니다.
     */
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 수정될 때 자동 실행됩니다.
     *
     * updatedAt만 현재 시간으로 갱신합니다.
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}