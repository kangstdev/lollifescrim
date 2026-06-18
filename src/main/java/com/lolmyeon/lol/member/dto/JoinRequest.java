package com.lolmyeon.lol.member.dto;

import com.lolmyeon.lol.common.LolLine;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequest {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private  String password;

    @NotBlank(message = "인증번호는 필수입니다.")
    private String inviteCode;

    @NotNull(message = "주라인은 필수입니다.")
    private LolLine mainLine;

    @NotNull(message = "부라인은 필수입니다.")
    private LolLine subLine;

    @NotBlank(message = "티어를 선택해주세요.")
    private String tier;
}
