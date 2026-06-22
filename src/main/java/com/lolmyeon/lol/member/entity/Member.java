package com.lolmyeon.lol.member.entity;

import com.lolmyeon.lol.common.LolLine;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 로그인 아이디처럼 사용할 닉네임
    @Column(nullable = false, unique = true, length = 30)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private LolLine mainLine;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LolLine subLine;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberRole role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "tier" , nullable = false)
    private String tier;

    /*
     * 탈퇴/삭제 처리 여부
     *
     * 실제 DB 삭제가 아니라 비활성화 처리합니다.
     */
    @Column(nullable = false)
    private boolean deleted = false;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.role == null) {
            this.role = MemberRole.USER;
        }
    }

    public void updateInfo(
            String nickname,
            String password,
            String tier,
            LolLine mainLine,
            LolLine subLine
    ) {
        this.nickname = nickname;
        this.password = password;
        this.tier = tier;
        this.mainLine = mainLine;
        this.subLine = subLine;
    }

    public void deactivate() {
        this.deleted = true;
        this.nickname = "탈퇴회원_" + this.id;
        this.password = "DELETED_ACCOUNT";
    }

    //비밀번호 해쉬처리
    public void changePassword(String password) {
        this.password = password;
    }

}
