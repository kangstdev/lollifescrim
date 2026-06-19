package com.lolmyeon.lol.member.dto;

import com.lolmyeon.lol.common.LolLine;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditMemberRequest {

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    /**
     * 비밀번호는 비워두면 기존 비밀번호 유지
     */
    private String password;

    @NotBlank(message = "티어를 선택해주세요.")
    private String tier;

    @NotNull(message = "주라인을 선택해주세요.")
    private LolLine mainLine;

    @NotNull(message = "부라인을 선택해주세요.")
    private LolLine subLine;
}