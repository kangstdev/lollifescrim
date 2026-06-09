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

        Participation participation = participationRepository
                .findByMemberAndParticipationType(member, request.getParticipationType())
                .orElse(null);

        if (participation == null) {
            participation = Participation.builder()
                    .member(member)
                    .participationType(request.getParticipationType())
                    .selectedTimes(selectedTimes)
                    .selectedLine(request.getSelectedLine())
                    .build();

            participationRepository.save(participation);
            return;
        }

        participation.setSelectedTimes(selectedTimes);
        participation.setSelectedLine(request.getSelectedLine());
    }

    /**
     * 일반 내전일 때만 주라인/부라인 제한
     */
    private void validateLine(Member member, ParticipationType participationType, LolLine selectedLine) {
        if (participationType != ParticipationType.NORMAL) {
            return;
        }

        boolean canSelect =
                member.getMainLine() == selectedLine ||
                        member.getSubLine() == selectedLine;

        if (!canSelect) {
            throw new IllegalArgumentException("일반 내전은 주라인/부라인만 선택할 수 있습니다.");
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
}