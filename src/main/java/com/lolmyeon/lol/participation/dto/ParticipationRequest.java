package com.lolmyeon.lol.participation.dto;


import com.lolmyeon.lol.common.LolLine;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ParticipationRequest {

    // 참여 종류: 내전, 자유내전, 자유랭크, 증바람
    @NotNull(message = "참여 종류는 필수입니다.")
    private ParticipationType participationType;

    // 참여 가능 시간: 여러 개 선택 가능
    @NotEmpty(message = "참여 가능 시간을 최소 1개 이상 선택해주세요.")
    private List<String> selectedTimes;

    // 참여 라인: 하나만 선택
    @NotNull(message = "참여 라인을 선택해주세요.")
    private LolLine selectedLine;
}
