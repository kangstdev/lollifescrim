package com.lolmyeon.lol.Admin.service;

import com.lolmyeon.lol.member.entity.Member;
import com.lolmyeon.lol.member.repository.MemberRepository;
import com.lolmyeon.lol.participation.entity.Participation;
import com.lolmyeon.lol.participation.repository.ParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import java.util.ArrayList;
import java.util.Comparator;



@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final MemberRepository memberRepository;
    private final ParticipationRepository participationRepository;

    /*
     * 회원 목록 조회
     */
    @Transactional(readOnly = true)
    public List<Member> findAllMembers() {
        return memberRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    /*
     * 비밀번호 0000으로 초기화
     *
     * 아직 BCrypt 적용 전이라 평문 0000으로 저장합니다.
     */
    public void resetPassword(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        member.setPassword("0000");
    }

    /*
     * 전체 참여 현황 조회
     */
    /*
     * 관리자 참여 현황 조회
     *
     * type과 time을 선택하면 해당 조건에 맞는 참여만 보여줍니다.
     */
    @Transactional(readOnly = true)
    public List<Participation> findParticipations(
            ParticipationType type,
            String time
    ) {
        List<Participation> participations = participationRepository.findAllWithMemberOrderByCreatedAtDesc();

        return participations.stream()
                .filter(participation -> {
                    if (type == null) {
                        return true;
                    }

                    return participation.getParticipationType() == type;
                })
                .filter(participation -> {
                    if (time == null || time.isBlank()) {
                        return true;
                    }

                    return containsTime(participation.getSelectedTimes(), time);
                })
                .toList();
    }

    /*
     * 관리자 참여 관리 화면에서 보여줄 시간 목록
     *
     * 기본 시간 20, 21, 22는 항상 보여주고,
     * 실제 신청된 11, 19 같은 시간도 같이 보여줍니다.
     */
    @Transactional(readOnly = true)
    public List<String> findParticipationTimes() {
        List<String> times = new ArrayList<>();

        times.add("20");
        times.add("21");
        times.add("22");

        participationRepository.findAll().forEach(participation -> {
            String selectedTimes = participation.getSelectedTimes();

            if (selectedTimes == null || selectedTimes.isBlank()) {
                return;
            }

            String[] splitTimes = selectedTimes.split(",");

            for (String time : splitTimes) {
                String trimmedTime = time.trim();

                if (!trimmedTime.isBlank() && !times.contains(trimmedTime)) {
                    times.add(trimmedTime);
                }
            }
        });

        return times.stream()
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

    /*
     * selectedTimes가 "20,21" 같은 형태라서
     * 단순 contains가 아니라 정확히 시간 단위로 비교합니다.
     */
    private boolean containsTime(String selectedTimes, String targetTime) {
        if (selectedTimes == null || selectedTimes.isBlank()) {
            return false;
        }

        String[] times = selectedTimes.split(",");

        for (String time : times) {
            if (time.trim().equals(targetTime)) {
                return true;
            }
        }

        return false;
    }

    /*
     * 참여 강제 취소
     */
    public void deleteParticipation(Long participationId) {
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 참여 신청입니다."));

        participationRepository.delete(participation);
    }

    /*
     * 회원 삭제 처리
     *
     * 실제 DB 삭제가 아니라 비활성화 처리합니다.
     * 해당 회원의 참여 신청은 먼저 삭제합니다.
     */
    public void deactivateMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<Participation> participations = participationRepository.findAllByMemberId(memberId);
        participationRepository.deleteAll(participations);

        member.deactivate();
    }
}