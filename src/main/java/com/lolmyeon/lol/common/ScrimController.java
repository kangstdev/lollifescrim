package com.lolmyeon.lol.scrim;

import com.lolmyeon.lol.member.dto.LoginMember;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ScrimController {

    @GetMapping("/scrims")
    public String scrimList(HttpSession session, Model model) {
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        model.addAttribute("loginMember", loginMember);

        return "scrim/list";
    }
}