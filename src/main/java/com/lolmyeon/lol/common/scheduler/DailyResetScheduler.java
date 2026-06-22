package com.lolmyeon.lol.common.scheduler;

import com.lolmyeon.lol.participation.repository.ParticipationRepository;
import com.lolmyeon.lol.party.repository.PartyRepository;
import com.lolmyeon.lol.waiting.repository.WaitingParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DailyResetScheduler {

    private final WaitingParticipationRepository waitingParticipationRepository;
    private final ParticipationRepository participationRepository;
    private final PartyRepository partyRepository;

    /**
     * 매일 오전 5시 초기화
     *
     * 유지:
     * - 회원
     * - 공지사항
     * - 건의사항
     *
     * 삭제:
     * - 대기 신청
     * - 참여 신청
     * - 파티/사용자 설정 시간
     */
    @Transactional
    //  테스트용 - 분마다 초기화 확인용
    // @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    //  실제코드 - 매일 오전 5시 초기화
    @Scheduled(cron = "0 0 5 * * *", zone = "Asia/Seoul")
    public void resetDailyData() {
        // DB가 꼬이지 않도록 신청자 - 참여신청 - 파티 순으로 초기화
        // 회원정보 공지사항 건의사항은 초기화 항목에서 제외
        waitingParticipationRepository.deleteAll();
        participationRepository.deleteAll();
        partyRepository.deleteAll();

        System.out.println("[DailyResetScheduler] 매일 오전 5시 데이터 초기화 완료");
    }
}