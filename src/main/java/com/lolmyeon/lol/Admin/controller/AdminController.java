package com.lolmyeon.lol.Admin.controller;

import com.lolmyeon.lol.Admin.service.AdminService;
import com.lolmyeon.lol.member.dto.LoginMember;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /*
     * 관리자 메인 페이지
     */
    @GetMapping("/admin")
    public String adminMain(
            HttpSession session,
            Model model
    ) {
        LoginMember loginMember = getLoginMember(session);

        if (loginMember == null) {
            return "redirect:/login";
        }

        if (!isAdmin(loginMember)) {
            return "redirect:/main";
        }

        model.addAttribute("loginMember", loginMember);

        return "admin/main";
    }

    /*
     * 회원 관리
     */
    @GetMapping("/admin/members")
    public String members(
            HttpSession session,
            Model model
    ) {
        LoginMember loginMember = getLoginMember(session);

        if (loginMember == null) {
            return "redirect:/login";
        }

        if (!isAdmin(loginMember)) {
            return "redirect:/main";
        }

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("members", adminService.findAllMembers());

        return "admin/members";
    }

    /*
     * 회원 비밀번호 0000 초기화
     */
    @PostMapping("/admin/members/{memberId}/reset-password")
    public String resetPassword(
            @PathVariable Long memberId,
            HttpSession session
    ) {
        LoginMember loginMember = getLoginMember(session);

        if (loginMember == null) {
            return "redirect:/login";
        }

        if (!isAdmin(loginMember)) {
            return "redirect:/main";
        }

        adminService.resetPassword(memberId);

        return "redirect:/admin/members";
    }

    /*
     * 회원 삭제 처리
     *
     * 실제 DB 삭제가 아니라 비활성화 처리합니다.
     */
    @PostMapping("/admin/members/{memberId}/deactivate")
    public String deactivateMember(
            @PathVariable Long memberId,
            HttpSession session
    ) {
        LoginMember loginMember = getLoginMember(session);

        if (loginMember == null) {
            return "redirect:/login";
        }

        if (!isAdmin(loginMember)) {
            return "redirect:/main";
        }

        adminService.deactivateMember(memberId);

        return "redirect:/admin/members";
    }

    /*
     * 참여 관리
     */
    /*
     * 참여 관리
     *
     * type, time 조건으로 필터링해서 조회합니다.
     */
    @GetMapping("/admin/participations")
    public String participations(
            @RequestParam(required = false) ParticipationType type,
            @RequestParam(required = false) String time,
            HttpSession session,
            Model model
    ) {
        LoginMember loginMember = getLoginMember(session);

        if (loginMember == null) {
            return "redirect:/login";
        }

        if (!isAdmin(loginMember)) {
            return "redirect:/main";
        }

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("participationTypes", ParticipationType.values());
        model.addAttribute("times", adminService.findParticipationTimes());
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedTime", time);
        model.addAttribute("participations", adminService.findParticipations(type, time));

        return "admin/participations";
    }

    /*
     * 참여 강제 취소
     */
    @PostMapping("/admin/participations/{participationId}/delete")
    public String deleteParticipation(
            @PathVariable Long participationId,
            HttpSession session
    ) {
        LoginMember loginMember = getLoginMember(session);

        if (loginMember == null) {
            return "redirect:/login";
        }

        if (!isAdmin(loginMember)) {
            return "redirect:/main";
        }

        adminService.deleteParticipation(participationId);

        return "redirect:/admin/participations";
    }

    private LoginMember getLoginMember(HttpSession session) {
        return (LoginMember) session.getAttribute("loginMember");
    }

    private boolean isAdmin(LoginMember loginMember) {
        return "ADMIN".equals(String.valueOf(loginMember.getRole()));
    }


}