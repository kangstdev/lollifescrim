package com.lolmyeon.lol.waiting.controller;

import com.lolmyeon.lol.member.entity.Member;
import com.lolmyeon.lol.member.repository.MemberRepository;
import com.lolmyeon.lol.participation.entity.Participation;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import com.lolmyeon.lol.participation.repository.ParticipationRepository;
import com.lolmyeon.lol.waiting.entity.WaitingParticipation;
import com.lolmyeon.lol.waiting.repository.WaitingParticipationRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.lolmyeon.lol.member.dto.LoginMember;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WaitingController {

    private final WaitingParticipationRepository waitingParticipationRepository;
    private final ParticipationRepository participationRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/waiting/submit")
    public String submitWaiting(
            @RequestParam ParticipationType participationType,
            @RequestParam String selectedTime,
            @RequestParam String selectedLine,
            HttpSession session
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        Long memberId = loginMember.getId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        boolean alreadyWaiting = waitingParticipationRepository
                .findByMemberAndParticipationTypeAndSelectedTime(
                        member,
                        participationType,
                        selectedTime
                )
                .isPresent();

        if (alreadyWaiting) {
            return "redirect:/participation/form?type=" + participationType + "&waitingError=duplicate";
        }

        List<Participation> myParticipations =
                participationRepository.findAllByMemberOrderByCreatedAtDesc(member);

        boolean alreadyParticipatedSameTime = myParticipations.stream()
                .filter(p -> p.getParticipationType() == participationType)
                .anyMatch(p ->
                        p.getSelectedTimes() != null &&
                                p.getSelectedTimes().contains(selectedTime)
                );

        if (alreadyParticipatedSameTime) {
            return "redirect:/participation/form?type=" + participationType + "&waitingError=alreadyParticipated";
        }

        WaitingParticipation waiting = WaitingParticipation.builder()
                .member(member)
                .participationType(participationType)
                .selectedTime(selectedTime)
                .selectedLine(selectedLine)
                .build();

        waitingParticipationRepository.save(waiting);

        return "redirect:/participation/form?type=" + participationType + "&waitingSuccess=true";
    }
}