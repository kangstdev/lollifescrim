package com.lolmyeon.lol.suggestion.controller;

import com.lolmyeon.lol.member.dto.LoginMember;
import com.lolmyeon.lol.suggestion.entity.Suggestion;
import com.lolmyeon.lol.suggestion.entity.SuggestionStatus;
import com.lolmyeon.lol.suggestion.service.SuggestionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/suggestion")
public class SuggestionController {

    private final SuggestionService suggestionService;

    /*
     * 건의사항 목록
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
        model.addAttribute("suggestions", suggestionService.findAllSuggestions());

        return "suggestion/list";
    }

    /*
     * 건의사항 상세
     */
    @GetMapping("/{suggestionId}")
    public String detail(
            @PathVariable Long suggestionId,
            HttpSession session,
            Model model
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        Suggestion suggestion = suggestionService.findSuggestion(suggestionId);

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("suggestion", suggestion);
        model.addAttribute("statuses", SuggestionStatus.values());

        return "suggestion/detail";
    }

    /*
     * 건의사항 작성 화면
     *
     * 일반 회원도 작성 가능합니다.
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

        model.addAttribute("loginMember", loginMember);

        return "suggestion/form";
    }

    /*
     * 건의사항 작성 처리
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

        suggestionService.createSuggestion(
                loginMember.getId(),
                title,
                content
        );

        return "redirect:/suggestion/list";
    }

    /*
     * 건의사항 수정 화면
     *
     * 작성자 본인 또는 관리자만 접근 가능합니다.
     */
    @GetMapping("/{suggestionId}/edit")
    public String editForm(
            @PathVariable Long suggestionId,
            HttpSession session,
            Model model
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        Suggestion suggestion = suggestionService.findSuggestion(suggestionId);

        boolean admin = isAdmin(loginMember);
        boolean owner = suggestion.getWriter().getId().equals(loginMember.getId());

        if (!admin && !owner) {
            return "redirect:/suggestion/list";
        }

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("suggestion", suggestion);

        return "suggestion/form";
    }

    /*
     * 건의사항 수정 처리
     */
    @PostMapping("/{suggestionId}/edit")
    public String edit(
            @PathVariable Long suggestionId,
            @RequestParam String title,
            @RequestParam String content,
            HttpSession session
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        suggestionService.updateSuggestion(
                suggestionId,
                loginMember.getId(),
                isAdmin(loginMember),
                title,
                content
        );

        return "redirect:/suggestion/" + suggestionId;
    }

    /*
     * 건의사항 상태 변경
     *
     * 관리자만 가능합니다.
     */
    @PostMapping("/{suggestionId}/status")
    public String updateStatus(
            @PathVariable Long suggestionId,
            @RequestParam SuggestionStatus status,
            HttpSession session
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        if (!isAdmin(loginMember)) {
            return "redirect:/suggestion/" + suggestionId;
        }

        suggestionService.updateStatus(suggestionId, status);

        return "redirect:/suggestion/" + suggestionId;
    }

    /*
     * 건의사항 삭제
     *
     * 작성자 본인 또는 관리자만 가능합니다.
     */
    @PostMapping("/{suggestionId}/delete")
    public String delete(
            @PathVariable Long suggestionId,
            HttpSession session
    ) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        suggestionService.deleteSuggestion(
                suggestionId,
                loginMember.getId(),
                isAdmin(loginMember)
        );

        return "redirect:/suggestion/list";
    }

    private boolean isAdmin(LoginMember loginMember) {
        return "ADMIN".equals(String.valueOf(loginMember.getRole()));
    }
}