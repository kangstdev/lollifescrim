package com.lolmyeon.lol.member.controller;

import com.lolmyeon.lol.common.LolLine;
import com.lolmyeon.lol.member.dto.JoinRequest;
import com.lolmyeon.lol.member.dto.LoginMember;
import com.lolmyeon.lol.member.dto.LoginRequest;
import com.lolmyeon.lol.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 홈
     * - 로그인 안 되어 있으면 로그인 페이지
     * - 로그인 되어 있으면 메인 페이지
     */
    @GetMapping("/")
    public String home(HttpSession session) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        return "redirect:/main";
    }

    /**
     * 회원가입 화면
     */
    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("joinRequest", new JoinRequest());
        model.addAttribute("lines", LolLine.values());
        return "member/join";
    }

    /**
     * 회원가입 처리
     */
    /**
     * 회원가입 처리
     */
    @PostMapping("/join")
    public String join(
            @Valid @ModelAttribute JoinRequest joinRequest,
            BindingResult bindingResult,
            Model model
    ) {
        model.addAttribute("lines", LolLine.values());

        if (bindingResult.hasErrors()) {
            System.out.println("========== 회원가입 검증 실패 ==========");

            bindingResult.getFieldErrors().forEach(error -> {
                System.out.println("필드명: " + error.getField());
                System.out.println("입력값: " + error.getRejectedValue());
                System.out.println("메시지: " + error.getDefaultMessage());
                System.out.println("------------------------------------");
            });

            return "member/join";
        }

        try {
            memberService.join(joinRequest);
        } catch (IllegalArgumentException e) {
            System.out.println("========== 회원가입 처리 실패 ==========");
            System.out.println("메시지: " + e.getMessage());

            model.addAttribute("errorMessage", e.getMessage());
            return "member/join";
        }

        return "redirect:/login";
    }

    /**
     * 로그인 화면
     */
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "member/login";
    }

    /**
     * 로그인 처리
     */
    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute LoginRequest loginRequest,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "member/login";
        }

        try {
            LoginMember loginMember = memberService.login(loginRequest);
            session.setAttribute("loginMember", loginMember);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/login";
        }

        return "redirect:/main";
    }

    /**
     * 메인 화면
     *
     * 실제 JSP 위치:
     * src/main/webapp/WEB-INF/views/main/main.jsp
     *
     * 그래서 return은 "main"이 아니라 "main/main"
     */
    @GetMapping("/main")
    public String main(HttpSession session, Model model) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        model.addAttribute("loginMember", loginMember);

        return "main/main";
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}