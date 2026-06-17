package com.lolmyeon.lol.party.service;

import com.lolmyeon.lol.member.entity.Member;
import com.lolmyeon.lol.member.repository.MemberRepository;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import com.lolmyeon.lol.party.entity.Party;
import com.lolmyeon.lol.party.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 파티 관련 비즈니스 로직
 *
 * 역할:
 * - 파티 만들기
 * - 파티 전체 목록 조회
 * - 파티 종류별 목록 조회
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyService {

    private final PartyRepository partyRepository;
    private final MemberRepository memberRepository;

    /**
     * 파티 생성
     *
     * memberId:
     * - 현재 로그인한 사용자 ID
     *
     * participationType:
     * - NORMAL / FREE / FLEX / ARAM
     *
     * partyTime:
     * - "19", "20", "21" 같은 시간 문자열
     *
     * title:
     * - 파티 제목
     *
     * memo:
     * - 파티 설명
     */
    @Transactional
    public void createParty(
            Long memberId,
            ParticipationType participationType,
            String partyTime,
            String title,
            String memo
    ) {
        /*
         * 1. 파티 만든 회원 조회
         */
        Member creator = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        /*
         * 2. 참여 종류 검증
         */
        if (participationType == null) {
            throw new IllegalArgumentException("파티 종류를 선택해주세요.");
        }

        /*
         * 3. 시간 검증
         */
        if (partyTime == null || partyTime.isBlank()) {
            throw new IllegalArgumentException("파티 시간을 입력해주세요.");
        }

        int timeNumber;

        try {
            timeNumber = Integer.parseInt(partyTime);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("파티 시간은 숫자로 입력해주세요.");
        }

        if (timeNumber < 0 || timeNumber > 23) {
            throw new IllegalArgumentException("파티 시간은 0시부터 23시까지만 가능합니다.");
        }

        /*
         * DB에는 "09"가 아니라 "9"처럼 저장합니다.
         */
        String normalizedTime = String.valueOf(timeNumber);

        /*
         * 4. 제목 검증
         */
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("파티 제목을 입력해주세요.");
        }

        /*
         * 5. 파티 생성
         */
        Party party = Party.builder()
                .creator(creator)
                .participationType(participationType)
                .partyTime(normalizedTime)
                .title(title.trim())
                .memo(memo)
                .build();

        partyRepository.save(party);
    }

    /**
     * 전체 파티 목록 조회
     *
     * 최신순으로 조회합니다.
     */
    public List<Party> findAllParties() {
        return partyRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * 특정 종류의 파티 목록 조회
     *
     * 예:
     * - NORMAL 파티만 조회
     * - FREE 파티만 조회
     */
    public List<Party> findPartiesByType(ParticipationType participationType) {
        if (participationType == null) {
            return findAllParties();
        }

        return partyRepository.findAllByParticipationTypeOrderByCreatedAtDesc(participationType);
    }
}