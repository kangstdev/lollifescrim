package com.lolmyeon.lol.notice.controller;

import com.lolmyeon.lol.member.dto.LoginMember;
import com.lolmyeon.lol.notice.entity.Notice;
import com.lolmyeon.lol.notice.service.NoticeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    /*
     * 공지사항 목록
     *
     * 일반 회원도 볼 수 있습니다.
     */
    @GetMapping("/list")
    public String list(
            HttpSession session,
            Model model
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("notices", noticeService.findAllNotices());

        return "notice/list";
    }

    /*
     * 공지사항 상세
     *
     * 일반 회원도 볼 수 있습니다.
     */
    @GetMapping("/{noticeId}")
    public String detail(
            @PathVariable Long noticeId,
            HttpSession session,
            Model model
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        Notice notice = noticeService.findNotice(noticeId);

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("notice", notice);

        return "notice/detail";
    }

    /*
     * 공지사항 작성 화면
     *
     * 관리자만 접근 가능합니다.
     */
    @GetMapping("/write")
    public String writeForm(
            HttpSession session,
            Model model
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        if (!isAdmin(loginMember)) {
            return "redirect:/notice/list";
        }

        model.addAttribute("loginMember", loginMember);

        return "notice/form";
    }

    /*
     * 공지사항 작성 처리
     *
     * 관리자만 작성 가능합니다.
     */
    @PostMapping("/write")
    public String write(
            @RequestParam String title,
            @RequestParam String content,
            HttpSession session
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        if (!isAdmin(loginMember)) {
            return "redirect:/notice/list";
        }

        noticeService.createNotice(
                loginMember.getId(),
                title,
                content
        );

        return "redirect:/notice/list";
    }

    /*
     * 공지사항 수정 화면
     *
     * 관리자만 접근 가능합니다.
     */
    @GetMapping("/{noticeId}/edit")
    public String editForm(
            @PathVariable Long noticeId,
            HttpSession session,
            Model model
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        if (!isAdmin(loginMember)) {
            return "redirect:/notice/list";
        }

        Notice notice = noticeService.findNotice(noticeId);

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("notice", notice);

        return "notice/form";
    }

    /*
     * 공지사항 수정 처리
     *
     * 관리자만 수정 가능합니다.
     */
    @PostMapping("/{noticeId}/edit")
    public String edit(
            @PathVariable Long noticeId,
            @RequestParam String title,
            @RequestParam String content,
            HttpSession session
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        if (!isAdmin(loginMember)) {
            return "redirect:/notice/list";
        }

        noticeService.updateNotice(
                noticeId,
                title,
                content
        );

        return "redirect:/notice/" + noticeId;
    }

    /*
     * 공지사항 삭제
     *
     * 관리자만 삭제 가능합니다.
     */
    @PostMapping("/{noticeId}/delete")
    public String delete(
            @PathVariable Long noticeId,
            HttpSession session
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        if (!isAdmin(loginMember)) {
            return "redirect:/notice/list";
        }

        noticeService.deleteNotice(noticeId);

        return "redirect:/notice/list";
    }

    /*
     * 관리자 여부 확인
     *
     * LoginMember의 role 타입이 enum이어도,
     * 문자열이어도 안전하게 비교하려고 String.valueOf() 사용합니다.
     */
    private boolean isAdmin(LoginMember loginMember) {
        return "ADMIN".equals(String.valueOf(loginMember.getRole()));
    }
}