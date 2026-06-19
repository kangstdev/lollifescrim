package com.lolmyeon.lol.participation.controller;

import com.lolmyeon.lol.common.LolLine;
import com.lolmyeon.lol.member.dto.LoginMember;
import com.lolmyeon.lol.participation.dto.ParticipationRequest;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import com.lolmyeon.lol.participation.service.ParticipationService;
import com.lolmyeon.lol.party.service.PartyService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/participation")
public class ParticipationController {

    private final ParticipationService participationService;
    private final PartyService partyService;

    /**
     * 참여 신청 화면
     *
     * 사용 예:
     * /participation/form?type=NORMAL
     * /participation/form?type=FREE
     * /participation/form?type=FLEX
     * /participation/form?type=ARAM
     */
    @GetMapping("/form")
    public String form(
            @RequestParam ParticipationType type,
            HttpSession session,
            Model model
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        ParticipationRequest request = new ParticipationRequest();
        request.setParticipationType(type);

        Set<String> occupiedSlotKeys =
                participationService.findOccupiedSlotKeys(type, loginMember.getId());

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("participationRequest", request);
        model.addAttribute("participationType", type);
        model.addAttribute("lines", LolLine.values());
        model.addAttribute("times", getDefaultTimes());

        /*
         * 파티 만들기에서 생성된 시간
         *
         * 예:
         * FLEX 19시 파티 생성
         * → 자유랭크 참여 신청 화면의 사용자 설정 시간에 19시 표시
         */
        model.addAttribute("partyTimes", partyService.findPartyTimesByType(type));

        /*
         * 이미 신청된 시간/라인 마감 처리용 데이터
         */
        model.addAttribute("occupiedSlotKeys", occupiedSlotKeys);

        return "participation/form";
    }

    /**
     * 참여 신청 저장
     */
    @PostMapping
    public String submit(
            @Valid @ModelAttribute ParticipationRequest participationRequest,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        /*
         * 증바람이 아닐 때만 라인 필수 검사
         */
        if (participationRequest.getParticipationType() != null
                && participationRequest.getParticipationType().isLineRequired()
                && participationRequest.getSelectedLine() == null) {

            bindingResult.rejectValue(
                    "selectedLine",
                    "required",
                    "참여 라인을 선택해주세요."
            );
        }

        if (bindingResult.hasErrors()) {
            addParticipationFormAttributes(
                    model,
                    loginMember,
                    participationRequest,
                    participationRequest.getParticipationType(),
                    null
            );

            return "participation/form";
        }

        try {
            participationService.saveParticipation(loginMember.getId(), participationRequest);

        } catch (IllegalArgumentException e) {
            addParticipationFormAttributes(
                    model,
                    loginMember,
                    participationRequest,
                    participationRequest.getParticipationType(),
                    e.getMessage()
            );

            return "participation/form";
        }

        /*
         * 신청 저장 성공 후 내 참여 현황 페이지로 이동
         */
        return "redirect:/participation/status";
    }

    /**
     * 내 참여 현황 페이지
     */
    @GetMapping("/status")
    public String status(
            HttpSession session,
            Model model
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("participations", participationService.findMyParticipations(loginMember.getId()));

        return "participation/status";
    }

    /**
     * 참여 신청 취소
     */
    @PostMapping("/cancel")
    public String cancel(
            @RequestParam Long participationId,
            HttpSession session,
            Model model
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        try {
            participationService.cancelParticipation(loginMember.getId(), participationId);

        } catch (IllegalArgumentException e) {
            model.addAttribute("loginMember", loginMember);
            model.addAttribute("participations", participationService.findMyParticipations(loginMember.getId()));
            model.addAttribute("errorMessage", e.getMessage());

            return "participation/status";
        }

        return "redirect:/participation/status";
    }

    /**
     * 다른 사람 참여현황 보기
     *
     * 사용 예:
     * /participation/list?type=NORMAL
     * /participation/list?type=FLEX&time=21
     */
    @GetMapping("/list")
    public String list(
            @RequestParam ParticipationType type,
            @RequestParam(required = false) String time,
            HttpSession session,
            Model model
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        String[] defaultTimes = getDefaultTimes();

        if (time == null || time.isBlank()) {
            time = defaultTimes[0];
        }

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("participationType", type);
        model.addAttribute("participations", participationService.findByType(type));
        model.addAttribute("times", participationService.findAvailableTimes(type, defaultTimes));
        model.addAttribute("selectedTime", time);
        model.addAttribute("lines", LolLine.values());

        return "participation/list";
    }

    /**
     * form.jsp로 다시 돌아갈 때 필요한 공통 model 데이터
     *
     * 검증 실패 / 서비스 예외 발생 시에도
     * lines, times, partyTimes, occupiedSlotKeys가 없으면 화면이 깨질 수 있어서
     * 공통 메서드로 묶었습니다.
     */
    private void addParticipationFormAttributes(
            Model model,
            LoginMember loginMember,
            ParticipationRequest participationRequest,
            ParticipationType participationType,
            String errorMessage
    ) {
        Set<String> occupiedSlotKeys =
                participationService.findOccupiedSlotKeys(
                        participationType,
                        loginMember.getId()
                );

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("participationRequest", participationRequest);
        model.addAttribute("participationType", participationType);
        model.addAttribute("lines", LolLine.values());
        model.addAttribute("times", getDefaultTimes());
        model.addAttribute("partyTimes", partyService.findPartyTimesByType(participationType));
        model.addAttribute("occupiedSlotKeys", occupiedSlotKeys);

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
    }
    /**
     * 기본 참여 가능 시간
     */
    private String[] getDefaultTimes() {
        return new String[]{"20", "21", "22"};
    }
}