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

import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/participation")
public class ParticipationController {

    private final ParticipationService participationService;

    /**
     * 참여 신청 화면
     *
     * 사용 예:
     * /participation/form?type=NORMAL
     * /participation/form?type=FREE
     * /participation/form?type=FLEX
     * /participation/form?type=ARAM
     *
     * 역할:
     * 1. 로그인 여부 확인
     * 2. 신청 폼에 필요한 기본 데이터 생성
     * 3. 이미 다른 사람이 신청한 자리 목록 조회
     * 4. JSP 화면으로 데이터 전달
     */
    @GetMapping("/form")
    public String form(
            @RequestParam ParticipationType type,
            HttpSession session,
            Model model
    ) {
        /*
         * 세션에서 로그인 회원 정보 가져오기
         *
         * 로그인 성공 시 세션에 "loginMember"라는 이름으로
         * LoginMember 객체가 저장되어 있다고 가정합니다.
         */
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        /*
         * 로그인하지 않은 사용자가 신청 페이지에 접근하면
         * 로그인 페이지로 이동시킵니다.
         */
        if (loginMember == null) {
            return "redirect:/login";
        }

        /*
         * 참여 신청 폼에서 사용할 request 객체 생성
         *
         * 사용자가 /participation/form?type=NORMAL 로 들어오면
         * participationType에 NORMAL을 미리 넣어둡니다.
         *
         * 이렇게 해야 JSP에서 현재 신청 종류를 알 수 있습니다.
         */
        ParticipationRequest request = new ParticipationRequest();
        request.setParticipationType(type);

        /*
         * 이미 다른 사람이 신청한 자리 목록 조회
         *
         * 예:
         * 누군가 일반내전 21시 MID를 신청했다면
         * occupiedSlotKeys 안에 "21_MID"가 들어갑니다.
         *
         * 누군가 자유내전 20시 TOP을 신청했다면
         * occupiedSlotKeys 안에 "20_TOP"이 들어갑니다.
         *
         * 현재 로그인한 회원의 신청은 제외합니다.
         * 이유:
         * 내가 이미 신청한 내역을 수정할 때
         * 내 기존 선택값까지 막히면 수정이 불편하기 때문입니다.
         */
        Set<String> occupiedSlotKeys =
                participationService.findOccupiedSlotKeys(type, loginMember.getId());

        /*
         * 신청 폼 화면에서 필요한 값들을 model에 담습니다.
         *
         * participation/form.jsp 에서 아래 값들을 사용할 수 있습니다.
         */
        model.addAttribute("loginMember", loginMember);
        model.addAttribute("participationRequest", request);
        model.addAttribute("participationType", type);
        model.addAttribute("lines", LolLine.values());
        model.addAttribute("times", getDefaultTimes());

        /*
         * JSP에서 이미 신청된 버튼을 막기 위한 데이터입니다.
         *
         * 예:
         * occupiedSlotKeys = ["21_MID", "20_TOP"]
         *
         * JSP에서 현재 버튼이 21시 MID라면
         * "21_MID" 키를 만들어서 occupiedSlotKeys에 있는지 확인하고,
         * 있으면 disabled 처리하면 됩니다.
         */
        model.addAttribute("occupiedSlotKeys", occupiedSlotKeys);

        return "participation/form";
    }

    /**
     * 참여 신청 저장
     *
     * 사용자가 신청 폼에서 시간을 선택하고,
     * 라인을 선택한 뒤 신청 버튼을 누르면 이 메서드로 들어옵니다.
     *
     * 역할:
     * 1. 로그인 여부 확인
     * 2. 라인 필수 선택 검증
     * 3. 검증 실패 시 다시 form 화면으로 이동
     * 4. Service를 통해 신청 저장
     * 5. 저장 성공 시 내 참여 현황 페이지로 이동
     */
    @PostMapping
    public String submit(
            @Valid @ModelAttribute ParticipationRequest participationRequest,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {
        /*
         * 현재 로그인한 회원 정보 가져오기
         */
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        /*
         * 로그인하지 않은 상태에서 신청 요청이 들어오면 로그인 페이지로 이동
         */
        if (loginMember == null) {
            return "redirect:/login";
        }

        /*
         * 증바람(ARAM)이 아닐 때만 라인 필수 검사
         *
         * NORMAL = 일반내전   → 라인 필요
         * FREE   = 자유내전   → 라인 필요
         * FLEX   = 자유랭크   → 라인 필요
         * ARAM   = 증바람     → 라인 필요 없음
         *
         * 즉, isLineRequired()가 true인 참여 종류는
         * 반드시 selectedLine이 있어야 합니다.
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

        /*
         * 유효성 검사에서 에러가 발생했을 때
         * 다시 form.jsp로 돌아가야 합니다.
         *
         * 그런데 form.jsp에서는
         * lines, times, occupiedSlotKeys 같은 값들이 필요합니다.
         *
         * 그래서 에러가 발생해도 화면이 깨지지 않도록
         * 다시 model에 필요한 값을 넣어줍니다.
         */
        if (bindingResult.hasErrors()) {
            Set<String> occupiedSlotKeys =
                    participationService.findOccupiedSlotKeys(
                            participationRequest.getParticipationType(),
                            loginMember.getId()
                    );

            model.addAttribute("loginMember", loginMember);
            model.addAttribute("participationRequest", participationRequest);
            model.addAttribute("participationType", participationRequest.getParticipationType());
            model.addAttribute("lines", LolLine.values());
            model.addAttribute("times", getDefaultTimes());
            model.addAttribute("occupiedSlotKeys", occupiedSlotKeys);

            return "participation/form";
        }

        try {
            /*
             * 실제 신청 저장 또는 수정 로직은 Service에서 처리합니다.
             *
             * Service에서는 다음과 같은 검사를 합니다.
             * - 회원 존재 여부
             * - 일반내전은 주라인/부라인만 선택 가능한지
             * - 이미 같은 시간/라인에 다른 사람이 신청했는지
             */
            participationService.saveParticipation(loginMember.getId(), participationRequest);

        } catch (IllegalArgumentException e) {
            /*
             * Service에서 예외가 발생하면 다시 form.jsp로 이동합니다.
             *
             * 예:
             * - 일반내전인데 주라인/부라인이 아닌 라인을 선택한 경우
             * - 이미 해당 시간에 같은 라인 신청자가 있는 경우
             *
             * 이때도 form.jsp에 필요한 값들을 다시 넣어줘야 합니다.
             */
            Set<String> occupiedSlotKeys =
                    participationService.findOccupiedSlotKeys(
                            participationRequest.getParticipationType(),
                            loginMember.getId()
                    );

            model.addAttribute("loginMember", loginMember);
            model.addAttribute("participationRequest", participationRequest);
            model.addAttribute("participationType", participationRequest.getParticipationType());
            model.addAttribute("lines", LolLine.values());
            model.addAttribute("times", getDefaultTimes());
            model.addAttribute("occupiedSlotKeys", occupiedSlotKeys);
            model.addAttribute("errorMessage", e.getMessage());

            return "participation/form";
        }

        /*
         * 신청 저장 성공 후 내 참여 현황 페이지로 이동
         */
        return "redirect:/participation/status";
    }

    /**
     * 내 참여 현황 페이지
     *
     * 사용자가 본인이 신청한 내역을 확인하는 화면입니다.
     *
     * 예:
     * - 일반내전 21시 MID 신청
     * - 자유랭크 22시 ADC 신청
     */
    @GetMapping("/status")
    public String status(
            HttpSession session,
            Model model
    ) {
        /*
         * 현재 로그인 회원 조회
         */
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        /*
         * 로그인하지 않은 사용자는 로그인 페이지로 이동
         */
        if (loginMember == null) {
            return "redirect:/login";
        }

        /*
         * 내 신청 내역 조회 후 JSP로 전달
         */
        model.addAttribute("loginMember", loginMember);
        model.addAttribute("participations", participationService.findMyParticipations(loginMember.getId()));

        return "participation/status";
    }

    /**
     * 참여 신청 취소
     *
     * 내 참여 현황 페이지에서 취소 버튼을 눌렀을 때 실행됩니다.
     */
    @PostMapping("/cancel")
    public String cancel(
            @RequestParam Long participationId,
            HttpSession session,
            Model model
    ) {
        /*
         * 현재 로그인 회원 조회
         */
        LoginMember loginMember = (LoginMember) session.getAttribute("loginMember");

        /*
         * 로그인하지 않은 사용자는 로그인 페이지로 이동
         */
        if (loginMember == null) {
            return "redirect:/login";
        }

        try {
            /*
             * 신청 취소는 Service에서 처리합니다.
             *
             * Service에서는
             * "본인의 신청인지" 확인한 뒤 삭제합니다.
             */
            participationService.cancelParticipation(loginMember.getId(), participationId);

        } catch (IllegalArgumentException e) {
            /*
             * 취소 중 오류가 발생하면
             * 다시 내 참여 현황 페이지를 보여줍니다.
             *
             * 예:
             * - 존재하지 않는 신청 내역
             * - 다른 사람의 신청을 취소하려고 한 경우
             */
            model.addAttribute("loginMember", loginMember);
            model.addAttribute("participations", participationService.findMyParticipations(loginMember.getId()));
            model.addAttribute("errorMessage", e.getMessage());

            return "participation/status";
        }

        /*
         * 취소 성공 후 내 참여 현황 페이지로 다시 이동
         */
        return "redirect:/participation/status";
    }

    /**
     * 기본 참여 가능 시간
     *
     * 현재는 MVP 단계라서 DB에 시간 테이블을 따로 만들지 않고
     * 코드에 20시, 21시, 22시를 고정해둡니다.
     *
     * 추후 기능:
     * - 사용자가 직접 시간 추가
     * - 관리자가 시간대 관리
     * - 19시, 23시 등 자유 시간 추가
     */
    private String[] getDefaultTimes() {
        return new String[]{"20", "21", "22"};
    }

    // 다른 사람 참여현황 보기
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

        String[] times = getDefaultTimes();

        /*
         * 주소에서 time 값이 없으면 기본으로 첫 번째 시간 선택
         * 현재 기본 시간은 20, 21, 22
         */
        if (time == null || time.isBlank()) {
            time = times[0];
        }

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("participationType", type);
        model.addAttribute("participations", participationService.findByType(type));
        model.addAttribute("times", participationService.findAvailableTimes(type, getDefaultTimes()));        model.addAttribute("selectedTime", time);
        model.addAttribute("lines", LolLine.values());

        return "participation/list";
    }
}

