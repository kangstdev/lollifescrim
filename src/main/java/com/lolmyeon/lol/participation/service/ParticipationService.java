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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final MemberRepository memberRepository;

    /**
     * 참여 신청 저장 또는 수정
     *
     * 현재 동작 순서:
     * 1. 회원 조회
     * 2. 참여 종류 / 라인 선택 가능 여부 검사
     * 3. 선택한 시간들을 문자열로 변환
     * 4. 실제 저장할 라인 결정
     * 5. 같은 참여종류 + 같은 라인 + 같은 시간 중복 신청 검사
     * 6. 기존 신청이 있으면 수정
     * 7. 기존 신청이 없으면 새로 저장
     */
    @Transactional
    public void saveParticipation(Long memberId, ParticipationRequest request) {
        // 1. 신청한 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        // 2. 참여 종류 / 라인 선택 가능 여부 검사
        validateLine(member, request.getParticipationType(), request.getSelectedLine());

        // 3. 선택한 시간들을 DB에 저장하기 위해 문자열로 변환
        // 예: ["20", "21"] -> "20,21"
        String selectedTimes = String.join(",", request.getSelectedTimes());

        // 4. 실제 DB에 저장할 라인
        // ARAM처럼 라인이 필요 없는 타입은 null 저장
        LolLine saveLine = null;

        if (request.getParticipationType().isLineRequired()) {
            saveLine = request.getSelectedLine();
        }

        /*
         * [운영진 확인 필요]
         *
         * 같은 회원이 같은 시간대에 다른 참여 종류를 중복 신청하지 못하게 막는 로직.
         *
         * 예)
         * - 일반내전 20시 신청 완료
         * - 자유랭크 20시 신청 시도
         * → 신청 불가 처리
         *
         * 다만 운영상으로는
         * "내전 인원이 안 차면 자유랭크를 하려고 둘 다 신청"
         * 하는 경우가 있을 수 있음.
         *
         * 따라서 현재는 적용하지 않고 주석으로 보관.
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
                    if (existingTime.trim().equals(requestTime.trim())) {
                        throw new IllegalArgumentException(
                                "이미 같은 시간에 다른 참여 신청이 있습니다. 운영진에게 확인해주세요."
                        );
                    }
                }
            }
        }
        */

        // 5. 다른 사람이 이미 같은 시간 / 같은 라인 / 같은 참여종류로 신청했는지 검사
        // 화면에서 버튼을 disabled 처리하더라도, 직접 요청이 들어올 수 있기 때문에 서버에서도 한 번 더 막습니다.
        validateDuplicateParticipationSlot(
                member,
                request.getParticipationType(),
                saveLine,
                request.getSelectedTimes()
        );

        // 6. 같은 회원이 같은 참여 종류로 이미 신청한 내역이 있는지 조회
        // 있으면 수정, 없으면 새로 저장
        Participation participation = participationRepository
                .findByMemberAndParticipationType(member, request.getParticipationType())
                .orElse(null);

        // 7. 기존 신청이 없으면 새로 저장
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

        // 8. 기존 신청이 있으면 시간 / 라인만 수정
        participation.setSelectedTimes(selectedTimes);
        participation.setSelectedLine(saveLine);
    }

    /**
     * 참여 타입별 라인 검증
     *
     * NORMAL = 일반내전
     * - 라인 필수
     * - 회원의 주라인 / 부라인만 선택 가능
     *
     * FREE = 자유내전
     * - 라인 필수
     *
     * FLEX = 자유랭크
     * - 라인 필수
     *
     * ARAM = 증바람
     * - 라인 필요 없음
     */
    private void validateLine(Member member, ParticipationType participationType, LolLine selectedLine) {
        // 참여 종류가 없으면 신청 불가
        if (participationType == null) {
            throw new IllegalArgumentException("참여 종류는 필수입니다.");
        }

        // 증바람처럼 라인이 필요 없는 참여 종류는 여기서 검사 종료
        if (!participationType.isLineRequired()) {
            return;
        }

        // 일반내전, 자유내전, 자유랭크는 라인 선택 필수
        if (selectedLine == null) {
            throw new IllegalArgumentException("참여 라인을 선택해주세요.");
        }

        // 일반내전은 본인의 주라인 / 부라인만 선택 가능
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
     * 같은 참여종류 + 같은 라인 + 같은 시간 중복 신청 방지
     *
     * 이 메서드는 저장 직전에 호출됩니다.
     *
     * 막는 예시:
     * - A회원: 일반내전 / 21시 / MID 신청 완료
     * - B회원: 일반내전 / 21시 / MID 신청 시도
     * → 신청 불가
     *
     * 허용되는 예시:
     * - A회원: 일반내전 / 21시 / MID 신청 완료
     * - A회원: 일반내전 / 22시 / MID로 수정
     * → 본인 수정이므로 가능
     *
     * - A회원: 일반내전 / 21시 / MID 신청 완료
     * - B회원: 일반내전 / 21시 / TOP 신청
     * → 라인이 다르므로 가능
     */
    private void validateDuplicateParticipationSlot(
            Member member,
            ParticipationType participationType,
            LolLine selectedLine,
            List<String> requestTimes
    ) {
        /*
         * 증바람(ARAM)
         * - 라인이 없음
         * - 같은 시간에 5명까지 한 파티
         * - 지금 단계에서는 5명 초과를 막지 않고, 나중에 화면에서 5명씩 파티를 나누는 방식으로 처리
         */
        if (participationType == ParticipationType.ARAM) {
            return;
        }

        /*
         * 라인이 필요한 타입만 여기부터 검사
         * NORMAL = 일반 내전
         * FREE = 자유 내전
         * FLEX = 자유랭크
         */
        if (!participationType.isLineRequired()) {
            return;
        }

        /*
         * 타입별 같은 시간 + 같은 라인 허용 인원
         */
        int maxCountPerTimeAndLine;

        if (participationType == ParticipationType.NORMAL || participationType == ParticipationType.FREE) {
            maxCountPerTimeAndLine = 2;
        } else if (participationType == ParticipationType.FLEX) {
            maxCountPerTimeAndLine = 1;
        } else {
            maxCountPerTimeAndLine = 1;
        }

        /*
         * 같은 참여종류 + 같은 라인으로 신청된 기존 신청 목록 조회
         */
        List<Participation> sameLineParticipations =
                participationRepository.findAllByParticipationTypeAndSelectedLine(
                        participationType,
                        selectedLine
                );

        /*
         * 사용자가 신청하려는 시간 하나씩 검사
         */
        for (String requestTime : requestTimes) {
            int count = 0;

            for (Participation existingParticipation : sameLineParticipations) {
                /*
                 * 본인이 기존에 신청한 같은 참여종류는 수정 가능해야 하므로 제외
                 */
                if (existingParticipation.getMember().getId().equals(member.getId())) {
                    continue;
                }

                String[] existingTimes = existingParticipation.getSelectedTimes().split(",");

                for (String existingTime : existingTimes) {
                    if (existingTime.trim().equals(requestTime.trim())) {
                        count++;
                    }
                }
            }

            /*
             * 이미 허용 인원만큼 차 있으면 신청 불가
             */
            if (count >= maxCountPerTimeAndLine) {
                throw new IllegalArgumentException(
                        "이미 해당 시간에 해당 라인 신청 인원이 가득 찼습니다."
                );
            }
        }
    }

    /**
     * 이미 다른 사람이 신청한 자리 목록 조회
     *
     * 이 메서드는 신청 화면에서 버튼을 비활성화하기 위해 사용합니다.
     *
     * 예)
     * 누군가 일반내전 21시 MID를 신청했다면
     * "21_MID" 라는 문자열을 만들어서 Set에 담습니다.
     *
     * JSP에서는 현재 버튼이 21시 MID 버튼이면
     * 똑같이 "21_MID" 키를 만든 뒤,
     * occupiedSlotKeys 안에 있는지 확인해서 disabled 처리하면 됩니다.
     *
     * 반환 예:
     * - 21_MID
     * - 20_TOP
     * - 22_JUNGLE
     *
     * currentMemberId를 받는 이유:
     * - 현재 로그인한 사용자의 신청은 제외하기 위해서입니다.
     * - 내가 이미 신청한 걸 수정할 때 내 기존 자리까지 막히면 불편하기 때문입니다.
     */
    public Set<String> findOccupiedSlotKeys(
            ParticipationType participationType,
            Long currentMemberId
    ) {
        // 이미 신청된 자리 키들을 담을 Set
        // Set을 쓰는 이유는 중복 값을 자동으로 제거하기 위해서입니다.
        Set<String> occupiedSlotKeys = new HashSet<>();

        // 참여 종류가 없으면 검사할 수 없으므로 빈 Set 반환
        if (participationType == null) {
            return occupiedSlotKeys;
        }

        // ARAM처럼 라인이 필요 없는 참여 종류는
        // 시간+라인 기준으로 막을 필요가 없으므로 빈 Set 반환
        if (!participationType.isLineRequired()) {
            return occupiedSlotKeys;
        }



        // 해당 참여 종류의 전체 신청 목록 조회
        // 예: 일반내전 신청 목록 전체 조회
        List<Participation> participations =
                participationRepository.findAllByParticipationTypeOrderByCreatedAtDesc(participationType);

        for (Participation participation : participations) {

            // 현재 로그인한 사용자의 신청은 제외
            // 이유: 본인 신청 수정 시 내 기존 선택값은 막히면 안 됨
            if (participation.getMember().getId().equals(currentMemberId)) {
                continue;
            }

            // 라인이 없는 데이터는 키를 만들 수 없으므로 제외
            if (participation.getSelectedLine() == null) {
                continue;
            }

            // DB에는 시간이 "20,21" 이런 문자열로 저장되어 있음
            // 그래서 콤마 기준으로 나눠서 각각 처리
            String[] times = participation.getSelectedTimes().split(",");

            for (String time : times) {
                // 예: time = "21", selectedLine = MID
                // 결과 key = "21_MID"
                String key = time.trim() + "_" + participation.getSelectedLine().name();

                // 이미 신청된 자리 목록에 추가
                occupiedSlotKeys.add(key);
            }
        }

        return occupiedSlotKeys;
    }

    /**
     * 참여 신청 화면과 현황 화면에서 사용할 시간 목록 조회
     *
     * 기본 시간:
     * - 20
     * - 21
     * - 22
     *
     * 추가 시간:
     * - 누군가 19시를 직접 추가해서 신청했다면
     * - DB selectedTimes에 "19"가 저장됨
     * - 그 값을 읽어서 시간 목록에 추가
     *
     * 반환 예:
     * - ["19", "20", "21", "22", "23"]
     */
    public List<String> findAvailableTimes(
            ParticipationType participationType,
            String[] defaultTimes
    ) {
        Set<String> timeSet = new HashSet<>();

        /*
         * 1. 기본 시간 먼저 추가
         */
        for (String time : defaultTimes) {
            timeSet.add(time);
        }

        /*
         * 2. 참여 종류가 없으면 기본 시간만 반환
         */
        if (participationType == null) {
            return new ArrayList<>(timeSet);
        }

        /*
         * 3. 해당 참여 종류의 기존 신청 목록 조회
         *
         * 예:
         * 일반내전 신청자들이 선택한 시간들을 전부 확인합니다.
         */
        List<Participation> participations =
                participationRepository.findAllByParticipationTypeOrderByCreatedAtDesc(participationType);

        /*
         * 4. DB에 저장된 selectedTimes에서 시간 추출
         *
         * 예:
         * "20,21" → 20, 21 추가
         * "19,22" → 19, 22 추가
         */
        for (Participation participation : participations) {
            String[] times = participation.getSelectedTimes().split(",");

            for (String time : times) {
                if (time != null && !time.trim().isEmpty()) {
                    timeSet.add(time.trim());
                }
            }
        }

        /*
         * 5. Set을 List로 바꾸고 숫자 순서대로 정렬
         *
         * 예:
         * 19, 20, 21, 22, 23
         */
        List<String> result = new ArrayList<>(timeSet);

        result.sort(Comparator.comparingInt(Integer::parseInt));

        return result;
    }

    /**
     * 특정 참여 종류의 신청 목록 조회
     *
     * 예:
     * - 일반내전 신청자 목록
     * - 자유내전 신청자 목록
     * - 자유랭크 신청자 목록
     * - 증바람 신청자 목록
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
     *
     * 본인의 신청만 취소 가능
     */
    @Transactional
    public void cancelParticipation(Long memberId, Long participationId) {
        // 현재 로그인한 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        // 취소하려는 신청 내역 조회
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new IllegalArgumentException("참여 신청 내역을 찾을 수 없습니다."));

        // 다른 사람의 신청은 취소할 수 없게 막기
        if (!participation.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("본인의 참여 신청만 취소할 수 있습니다.");
        }

        // 신청 삭제
        participationRepository.delete(participation);
    }
}