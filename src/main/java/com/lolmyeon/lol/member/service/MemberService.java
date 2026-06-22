package com.lolmyeon.lol.member.service;

import com.lolmyeon.lol.member.dto.EditMemberRequest;
import com.lolmyeon.lol.member.dto.JoinRequest;
import com.lolmyeon.lol.member.dto.LoginMember;
import com.lolmyeon.lol.member.dto.LoginRequest;
import com.lolmyeon.lol.member.entity.Member;
import com.lolmyeon.lol.member.entity.MemberRole;
import com.lolmyeon.lol.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.invite-code}")
    private String inviteCode;

    public void join(JoinRequest request) {
        if (!inviteCode.equals(request.getInviteCode())) {
            throw new IllegalArgumentException("인증번호가 올바르지 않습니다.");
        }

        if (memberRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.builder()
                .nickname(request.getNickname())
                .password(encodedPassword)
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

        if (member.isDeleted()) {
            throw new IllegalArgumentException("삭제 처리된 계정입니다.");
        }

        String storedPassword = member.getPassword();
        String inputPassword = request.getPassword();

        boolean passwordMatches = isPasswordMatches(inputPassword, storedPassword);

        if (!passwordMatches) {
            throw new IllegalArgumentException("닉네임 또는 비밀번호가 올바르지 않습니다.");
        }

        /*
         * 기존 평문 비밀번호 계정이면
         * 로그인 성공 시 BCrypt 해시 비밀번호로 자동 전환
         */
        if (!isBCryptPassword(storedPassword)) {
            member.changePassword(passwordEncoder.encode(inputPassword));
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

    @Transactional(readOnly = true)
    public EditMemberRequest getEditMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        EditMemberRequest request = new EditMemberRequest();
        request.setNickname(member.getNickname());
        request.setTier(member.getTier());
        request.setMainLine(member.getMainLine());
        request.setSubLine(member.getSubLine());

        return request;
    }

    public LoginMember editMember(Long memberId, EditMemberRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        if (!member.getNickname().equals(request.getNickname())) {
            if (memberRepository.existsByNickname(request.getNickname())) {
                throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
            }
        }

        String password = member.getPassword();

        /*
         * 회원정보 수정에서 비밀번호를 입력한 경우에만
         * 새 비밀번호를 BCrypt로 암호화해서 저장
         */
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            password = passwordEncoder.encode(request.getPassword());
        }

        member.updateInfo(
                request.getNickname(),
                password,
                request.getTier(),
                request.getMainLine(),
                request.getSubLine()
        );

        return new LoginMember(
                member.getId(),
                member.getNickname(),
                member.getMainLine(),
                member.getSubLine(),
                member.getRole(),
                member.getTier()
        );
    }

    /**
     * 입력 비밀번호와 DB 비밀번호 비교
     *
     * 기존 평문 비밀번호도 임시로 허용합니다.
     * 평문 계정은 로그인 성공 시 BCrypt로 자동 전환됩니다.
     */
    private boolean isPasswordMatches(String inputPassword, String storedPassword) {
        if (isBCryptPassword(storedPassword)) {
            return passwordEncoder.matches(inputPassword, storedPassword);
        }

        return storedPassword.equals(inputPassword);
    }

    /**
     * BCrypt 해시 비밀번호인지 확인
     */
    private boolean isBCryptPassword(String password) {
        return password != null
                && (
                password.startsWith("$2a$")
                        || password.startsWith("$2b$")
                        || password.startsWith("$2y$")
        );
    }
}