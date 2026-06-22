package com.lolmyeon.lol.participation.controller;

import com.lolmyeon.lol.common.LolLine;
import com.lolmyeon.lol.member.dto.LoginMember;
import com.lolmyeon.lol.participation.dto.ParticipationRequest;
import com.lolmyeon.lol.participation.entity.Participation;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import com.lolmyeon.lol.participation.service.ParticipationService;
import com.lolmyeon.lol.party.service.PartyService;
import com.lolmyeon.lol.waiting.entity.WaitingParticipation;
import com.lolmyeon.lol.waiting.repository.WaitingParticipationRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/participation")
public class ParticipationController {

    private final ParticipationService participationService;
    private final PartyService partyService;
    private final WaitingParticipationRepository waitingParticipationRepository;

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

        addParticipationFormAttributes(
                model,
                loginMember,
                request,
                type,
                null
        );

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
     * lines, times, partyTimes, occupiedSlotKeys, fullTimes, waitingMap이 없으면
     * 화면이 깨질 수 있어서 공통 메서드로 묶었습니다.
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

        String[] defaultTimes = getDefaultTimes();

        /*
         * partyService.findPartyTimesByType() 반환 타입이 List든 Set이든 처리되게
         * new ArrayList<>()로 감싸서 List로 맞춥니다.
         */
        List<String> partyTimes = new ArrayList<>(
                partyService.findPartyTimesByType(participationType)
        );

        List<String> allTimes = new ArrayList<>();
        allTimes.addAll(Arrays.asList(defaultTimes));
        allTimes.addAll(partyTimes);

        List<String> fullTimes = findFullTimes(participationType, allTimes);
        Map<String, List<WaitingParticipation>> waitingMap = findWaitingMap(participationType);

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("participationRequest", participationRequest);
        model.addAttribute("participationType", participationType);
        model.addAttribute("lines", LolLine.values());
        model.addAttribute("times", defaultTimes);
        model.addAttribute("partyTimes", partyTimes);
        model.addAttribute("occupiedSlotKeys", occupiedSlotKeys);

        /*
         * 대기 신청용 데이터
         */
        model.addAttribute("fullTimes", fullTimes);
        model.addAttribute("waitingMap", waitingMap);

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
    }

    /**
     * 인원이 가득 찬 시간 찾기
     *
     * 현재 1차 버전에서는 항목 상관없이
     * 해당 시간 신청자가 5명 이상이면 마감으로 판단합니다.
     *
     * 예:
     * 20시 신청자 5명 이상
     * → 20시 대기 신청 가능
     */
    private List<String> findFullTimes(
            ParticipationType type,
            List<String> times
    ) {
        List<Participation> participations = participationService.findByType(type);

        List<String> fullTimes = new ArrayList<>();

        int maxCount = getMaxParticipantCount(type);

        for (String time : times) {
            long count = participations.stream()
                    .filter(p -> p.getSelectedTimes() != null)
                    .filter(p -> Arrays.asList(p.getSelectedTimes().split(",")).contains(time))
                    .count();

            if (count >= maxCount) {
                fullTimes.add(time);
            }
        }

        return fullTimes;
    }

    private int getMaxParticipantCount(ParticipationType type) {
        if (type == ParticipationType.NORMAL || type == ParticipationType.FREE) {
            return 10;
        }

        return 5;
    }

    /**
     * 현재 항목의 대기자를 시간별로 묶기
     *
     * 예:
     * waitingMap.get("20")
     * → 20시 대기자 목록
     */
    private Map<String, List<WaitingParticipation>> findWaitingMap(
            ParticipationType type
    ) {
        List<WaitingParticipation> waitingList =
                waitingParticipationRepository.findAllByParticipationTypeOrderBySelectedTimeAscCreatedAtAsc(type);

        Map<String, List<WaitingParticipation>> waitingMap = new LinkedHashMap<>();

        for (WaitingParticipation waiting : waitingList) {
            waitingMap
                    .computeIfAbsent(waiting.getSelectedTime(), key -> new ArrayList<>())
                    .add(waiting);
        }

        return waitingMap;
    }

    /**
     * 기본 참여 가능 시간
     */
    private String[] getDefaultTimes() {
        return new String[]{"20", "21", "22"};
    }
}