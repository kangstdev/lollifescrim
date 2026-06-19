package com.lolmyeon.lol.party.service;

import com.lolmyeon.lol.common.LolLine;
import com.lolmyeon.lol.member.entity.Member;
import com.lolmyeon.lol.member.repository.MemberRepository;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import com.lolmyeon.lol.party.entiry.Party;
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
     */
    @Transactional
    public void createParty(
            Long memberId,
            ParticipationType participationType,
            String partyTime,
            String title,
            String memo,
            LolLine creatorLine
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
         * 3. 라인 검증
         *
         * NORMAL = 내전
         * FREE   = 자유내전
         * FLEX   = 자유랭크
         *
         * 위 3개는 파티장이 본인 라인을 선택해야 함.
         * ARAM = 증바람은 라인 없음.
         */
        if (participationType != ParticipationType.ARAM && creatorLine == null) {
            throw new IllegalArgumentException("라인을 선택해주세요.");
        }

        /*
         * 4. 시간 검증
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
         * 5. 제목 검증
         */
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("파티 제목을 입력해주세요.");
        }

        /*
         * 6. 파티 생성
         */
        Party party = Party.builder()
                .creator(creator)
                .participationType(participationType)
                .partyTime(normalizedTime)
                .creatorLine(creatorLine)
                .title(title.trim())
                .memo(memo)
                .build();

        partyRepository.save(party);
    }

    /**
     * 전체 파티 목록 조회
     */
    public List<Party> findAllParties() {
        return partyRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * 특정 종류의 파티 목록 조회
     */
    public List<Party> findPartiesByType(ParticipationType participationType) {
        if (participationType == null) {
            return findAllParties();
        }

        return partyRepository.findAllByParticipationTypeOrderByCreatedAtDesc(participationType);
    }

    /**
     * 특정 파티 종류의 사용자 설정 시간 목록 조회
     *
     * 예:
     * FLEX 타입 파티가 19시, 23시로 만들어져 있으면
     * 자유랭크 참여 신청 화면의 사용자 설정 시간에 19시, 23시 표시
     */
    @Transactional(readOnly = true)
    public List<String> findPartyTimesByType(ParticipationType participationType) {
        if (participationType == null) {
            return List.of();
        }

        return partyRepository.findAllByParticipationTypeOrderByCreatedAtDesc(participationType)
                .stream()
                .map(Party::getPartyTime)
                .filter(time -> time != null && !time.isBlank())
                .distinct()
                .sorted((a, b) -> {
                    try {
                        return Integer.compare(Integer.parseInt(a), Integer.parseInt(b));
                    } catch (NumberFormatException e) {
                        return a.compareTo(b);
                    }
                })
                .toList();
    }
}
