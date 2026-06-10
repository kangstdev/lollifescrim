package com.lolmyeon.lol.participation.service;

import com.lolmyeon.lol.common.LolLine;
import com.lolmyeon.lol.member.entity.Member;
import com.lolmyeon.lol.member.repository.MemberRepository;
import com.lolmyeon.lol.participation.dto.ParticipationRequest;
import com.lolmyeon.lol.participation.entity.Participation;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import com.lolmyeon.lol.participation.repository.ParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final MemberRepository memberRepository;

    /**
     * 참여 신청 저장 또는 수정
     */
    @Transactional
    public void saveParticipation(Long memberId, ParticipationRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        validateLine(member, request.getParticipationType(), request.getSelectedLine());

        String selectedTimes = String.join(",", request.getSelectedTimes());



        LolLine saveLine = null;

        /*
         * [운영진 확인 필요]
         *
         * 같은 회원이 같은 시간대에 다른 참여 종류를 중복 신청하지 못하게 막는 로직.
         *
         * 예)
         * - 내전 20시 신청 완료
         * - 자유랭크 20시 신청 시도
         * → 신청 불가 처리
         *
         * 다만 운영상으로는 "내전 인원이 안 차면 자유랭크를 하려고 둘 다 신청"하는 경우가 있을 수 있음.
         * 따라서 현재는 적용하지 않고 주석으로 보관.
         *
         * 운영진 의견에 따라 필요하면 아래 로직 주석 해제.
         */

/*
List<Participation> myParticipations =
        participationRepository.findAllByMemberOrderByCreatedAtDesc(member);

for (Participation existingParticipation : myParticipations) {
    // 같은 참여 종류는 기존 신청 수정으로 처리되므로 중복 검사 제외
    if (existingParticipation.getParticipationType() == request.getParticipationType()) {
        continue;
    }

    String[] existingTimes = existingParticipation.getSelectedTimes().split(",");

    for (String existingTime : existingTimes) {
        for (String requestTime : request.getSelectedTimes()) {
            if (existingTime.equals(requestTime)) {
                throw new IllegalArgumentException(
                        "이미 같은 시간에 다른 참여 신청이 있습니다. 운영진에게 확인해주세요."
                );
            }
        }
    }
}
*/



        if (request.getParticipationType().isLineRequired()) {
            saveLine = request.getSelectedLine();
        }

        Participation participation = participationRepository
                .findByMemberAndParticipationType(member, request.getParticipationType())
                .orElse(null);

        if (participation == null) {
            participation = Participation.builder()
                    .member(member)
                    .participationType(request.getParticipationType())
                    .selectedTimes(selectedTimes)
                    .selectedLine(saveLine)
                    .build();

            participationRepository.save(participation);
            return;
        }

        participation.setSelectedTimes(selectedTimes);
        participation.setSelectedLine(saveLine);
    }

    /**
     * 참여 타입별 라인 검증
     *
     * NORMAL = 내전     → 라인 필수 + 회원의 주라인/부라인만 가능
     * FREE   = 자유내전 → 라인 필수
     * FLEX   = 자유랭크 → 라인 필수
     * ARAM   = 증바람   → 라인 필요 없음
     */
    private void validateLine(Member member, ParticipationType participationType, LolLine selectedLine) {
        if (participationType == null) {
            throw new IllegalArgumentException("참여 종류는 필수입니다.");
        }

        // 증바람은 라인 필요 없음
        if (!participationType.isLineRequired()) {
            return;
        }

        // 내전, 자유내전, 자유랭크는 라인 필수
        if (selectedLine == null) {
            throw new IllegalArgumentException("참여 라인을 선택해주세요.");
        }

        // 일반 내전은 본인의 주라인/부라인만 선택 가능
        if (participationType == ParticipationType.NORMAL) {
            boolean canSelect =
                    member.getMainLine() == selectedLine ||
                            member.getSubLine() == selectedLine;

            if (!canSelect) {
                throw new IllegalArgumentException("일반 내전은 주라인/부라인만 선택할 수 있습니다.");
            }
        }
    }

    /**
     * 특정 참여 종류의 신청 목록 조회
     */
    public List<Participation> findByType(ParticipationType participationType) {
        return participationRepository.findAllByParticipationTypeOrderByCreatedAtDesc(participationType);
    }

    /**
     * 내 신청 내역 조회
     */
    public List<Participation> findMyParticipations(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        return participationRepository.findAllByMemberOrderByCreatedAtDesc(member);
    }
    /**
     * 참여 신청 취소
     */
    @Transactional
    public void cancelParticipation(Long memberId, Long participationId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new IllegalArgumentException("참여 신청 내역을 찾을 수 없습니다."));

        if (!participation.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("본인의 참여 신청만 취소할 수 있습니다.");
        }

        participationRepository.delete(participation);
    }
}