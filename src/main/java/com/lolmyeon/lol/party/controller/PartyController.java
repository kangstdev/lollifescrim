package com.lolmyeon.lol.party.controller;

import com.lolmyeon.lol.member.dto.LoginMember;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import com.lolmyeon.lol.party.service.PartyService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 파티 관련 Controller
 *
 * 담당 기능:
 * 1. 파티 만들기 화면 보여주기
 * 2. 파티 생성 처리
 * 3. 파티 목록 화면 보여주기
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/party")
public class PartyController {

    private final PartyService partyService;

    /**
     * 파티 만들기 화면
     *
     * 주소:
     * /party/form
     *
     * 역할:
     * - 로그인 여부 확인
     * - 파티 종류 목록을 화면에 전달
     * - party/form.jsp로 이동
     */
    @GetMapping("/form")
    public String form(
            HttpSession session,
            Model model
    ) {
        /*
         * 세션에서 로그인 사용자 정보 조회
         */
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        /*
         * 로그인하지 않았으면 로그인 페이지로 이동
         */
        if (loginMember == null) {
            return "redirect:/login";
        }

        /*
         * JSP에서 사용할 데이터 전달
         *
         * participationTypes:
         * - NORMAL
         * - FREE
         * - FLEX
         * - ARAM
         */
        model.addAttribute("loginMember", loginMember);
        model.addAttribute("participationTypes", ParticipationType.values());

        return "party/form";
    }

    /**
     * 파티 생성 처리
     *
     * 주소:
     * POST /party
     *
     * form.jsp에서 입력한 값:
     * - participationType
     * - partyTime
     * - title
     * - memo
     */
    @PostMapping
    public String create(
            @RequestParam ParticipationType participationType,
            @RequestParam String partyTime,
            @RequestParam String title,
            @RequestParam(required = false) String memo,
            HttpSession session,
            Model model
    ) {
        /*
         * 세션에서 로그인 사용자 정보 조회
         */
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        /*
         * 로그인하지 않았으면 로그인 페이지로 이동
         */
        if (loginMember == null) {
            return "redirect:/login";
        }

        try {
            /*
             * 실제 파티 생성은 Service에서 처리
             */
            partyService.createParty(
                    loginMember.getId(),
                    participationType,
                    partyTime,
                    title,
                    memo
            );

        } catch (IllegalArgumentException e) {
            /*
             * 입력값에 문제가 있으면 다시 파티 만들기 화면으로 이동
             *
             * 예:
             * - 파티 종류 없음
             * - 시간 미입력
             * - 제목 미입력
             * - 시간이 0~23 범위 밖
             */
            model.addAttribute("loginMember", loginMember);
            model.addAttribute("participationTypes", ParticipationType.values());
            model.addAttribute("errorMessage", e.getMessage());

            /*
             * 사용자가 입력했던 값 다시 화면에 남겨두기
             */
            model.addAttribute("selectedParticipationType", participationType);
            model.addAttribute("partyTime", partyTime);
            model.addAttribute("title", title);
            model.addAttribute("memo", memo);

            return "party/form";
        }

        /*
         * 파티 생성 성공 후 파티 목록으로 이동
         */
        return "redirect:/party/list";
    }

    /**
     * 파티 목록 화면
     *
     * 주소:
     * /party/list
     *
     * 전체 파티 목록 보기:
     * /party/list
     *
     * 특정 종류만 보기:
     * /party/list?type=NORMAL
     * /party/list?type=FREE
     * /party/list?type=FLEX
     * /party/list?type=ARAM
     */
    @GetMapping("/list")
    public String list(
            @RequestParam(required = false) ParticipationType type,
            HttpSession session,
            Model model
    ) {
        /*
         * 세션에서 로그인 사용자 정보 조회
         */
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        /*
         * 로그인하지 않았으면 로그인 페이지로 이동
         */
        if (loginMember == null) {
            return "redirect:/login";
        }

        /*
         * 화면에 필요한 값 전달
         */
        model.addAttribute("loginMember", loginMember);
        model.addAttribute("participationTypes", ParticipationType.values());
        model.addAttribute("selectedType", type);

        /*
         * type이 있으면 해당 종류만 조회
         * type이 없으면 전체 조회
         */
        model.addAttribute("parties", partyService.findPartiesByType(type));

        return "party/list";
    }
}