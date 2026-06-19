package com.lolmyeon.lol.notice.entity;

import com.lolmyeon.lol.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * 공지 작성자
     * 관리자 회원이 작성합니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private Member writer;

    /*
     * 공지 제목
     */
    private String title;

    /*
     * 공지 내용
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    /*
     * 생성일
     */
    private LocalDateTime createdAt;

    /*
     * 수정일
     */
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /*
     * 공지 수정
     */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}