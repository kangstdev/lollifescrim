package com.lolmyeon.lol.member.service;

import com.lolmyeon.lol.member.dto.JoinRequest;
import com.lolmyeon.lol.member.dto.LoginMember;
import com.lolmyeon.lol.member.dto.LoginRequest;
import com.lolmyeon.lol.member.entity.Member;
import com.lolmyeon.lol.member.entity.MemberRole;
import com.lolmyeon.lol.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Value("${app.invite-code}")
    private String inviteCode;

    @Transactional
    public void join(JoinRequest request) {
        if (!inviteCode.equals(request.getInviteCode())) {
            throw new IllegalArgumentException("인증번호가 올바르지 않습니다.");
        }

        if (memberRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }



        Member member = Member.builder()
                .nickname(request.getNickname())
                .password(request.getPassword())
                .mainLine(request.getMainLine())
                .subLine(request.getSubLine())
                .tier(request.getTier())
                .role(MemberRole.USER)
                .build();

        memberRepository.save(member);
    }

    public LoginMember login(LoginRequest request) {
        Member member = memberRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new IllegalArgumentException("닉네임 또는 비밀번호가 올바르지 않습니다."));

        if (!member.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("닉네임 또는 비밀번호가 올바르지 않습니다.");
        }

        return new LoginMember(
                member.getId(),
                member.getNickname(),
                member.getMainLine(),
                member.getSubLine(),
                member.getRole(),
                member.getTier()
        );
    }
}