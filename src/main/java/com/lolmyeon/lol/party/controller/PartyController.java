package com.lolmyeon.lol.party.controller;

import com.lolmyeon.lol.common.LolLine;
import com.lolmyeon.lol.member.dto.LoginMember;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import com.lolmyeon.lol.party.service.PartyService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/party")
public class PartyController {

    private final PartyService partyService;

    /**
     * 파티 만들기 화면
     */
    @GetMapping("/form")
    public String form(
            HttpSession session,
            Model model
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("participationTypes", ParticipationType.values());
        model.addAttribute("lines", LolLine.values());

        return "party/form";
    }

    /**
     * 파티 생성 처리
     */
    @PostMapping
    public String create(
            @RequestParam ParticipationType participationType,
            @RequestParam String partyTime,
            @RequestParam String title,
            @RequestParam(required = false) String memo,
            @RequestParam(required = false) LolLine creatorLine,
            HttpSession session,
            Model model
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        try {
            partyService.createParty(
                    loginMember.getId(),
                    participationType,
                    partyTime,
                    title,
                    memo,
                    creatorLine
            );

        } catch (IllegalArgumentException e) {
            model.addAttribute("loginMember", loginMember);
            model.addAttribute("participationTypes", ParticipationType.values());
            model.addAttribute("lines", LolLine.values());
            model.addAttribute("errorMessage", e.getMessage());

            model.addAttribute("selectedParticipationType", participationType);
            model.addAttribute("partyTime", partyTime);
            model.addAttribute("title", title);
            model.addAttribute("memo", memo);
            model.addAttribute("creatorLine", creatorLine);

            return "party/form";
        }

        /*
         * 파티 생성 성공 후 파티 목록이 아니라
         * 해당 항목별 참여 현황으로 이동
         *
         * NORMAL -> /participation/status?type=NORMAL
         * FREE   -> /participation/status?type=FREE
         * FLEX   -> /participation/status?type=FLEX
         * ARAM   -> /participation/status?type=ARAM
         */
        return "redirect:/participation/status?type=" + participationType.name();
    }

    /**
     * 파티 목록 화면
     *
     * 지금은 안 쓸 예정이지만, 일단 남겨둠.
     * 나중에 완전히 안 쓰면 이 메서드는 삭제해도 됨.
     */
    @GetMapping("/list")
    public String list(
            @RequestParam(required = false) ParticipationType type,
            HttpSession session,
            Model model
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("participationTypes", ParticipationType.values());
        model.addAttribute("selectedType", type);
        model.addAttribute("parties", partyService.findPartiesByType(type));

        return "party/list";
    }
}