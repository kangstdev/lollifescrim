package com.lolmyeon.lol.member.dto;

import com.lolmyeon.lol.common.LolLine;
import com.lolmyeon.lol.member.entity.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginMember {

     private Long id;
     private String nickname;
     private LolLine mainLine;
     private LolLine subLine;
     private MemberRole role;



}
