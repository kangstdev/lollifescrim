package com.lolmyeon.lol.participation.controller;

import com.lolmyeon.lol.common.LolLine;
import com.lolmyeon.lol.member.dto.LoginMember;
import com.lolmyeon.lol.participation.dto.ParticipationRequest;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import com.lolmyeon.lol.participation.service.ParticipationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/participation")
public class ParticipationController {

    private final ParticipationService participationService;

    /**
     * 참여 신청 화면
     * 예:
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

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("participationRequest", request);
        model.addAttribute("participationType", type);
        model.addAttribute("lines", LolLine.values());
        model.addAttribute("times", getDefaultTimes());

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

        /**
         * 증바람(ARAM)이 아닐 때만 라인 필수 검사
         *
         * NORMAL = 내전       → 라인 필요
         * FREE   = 자유내전   → 라인 필요
         * FLEX   = 자유랭크   → 라인 필요
         * ARAM   = 증바람     → 라인 필요 없음
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
            model.addAttribute("loginMember", loginMember);
            model.addAttribute("participationType", participationRequest.getParticipationType());
            model.addAttribute("lines", LolLine.values());
            model.addAttribute("times", getDefaultTimes());
            return "participation/form";
        }

        try {
            participationService.saveParticipation(loginMember.getId(), participationRequest);
        } catch (IllegalArgumentException e) {
            model.addAttribute("loginMember", loginMember);
            model.addAttribute("participationType", participationRequest.getParticipationType());
            model.addAttribute("lines", LolLine.values());
            model.addAttribute("times", getDefaultTimes());
            model.addAttribute("errorMessage", e.getMessage());
            return "participation/form";
        }

        return "redirect:/participation/status";
    }

    /**
     * 내 참여 현황
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
     * 기본 참여 가능 시간
     * 오늘 MVP에서는 DB 말고 코드에 고정
     */
    private String[] getDefaultTimes() {
        return new String[]{"20", "21", "22"};
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

}

