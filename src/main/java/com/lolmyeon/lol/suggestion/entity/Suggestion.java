package com.lolmyeon.lol.suggestion.entity;

import com.lolmyeon.lol.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "suggestions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Suggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * 작성자
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private Member writer;

    /*
     * 제목
     */
    @Column(nullable = false, length = 100)
    private String title;

    /*
     * 내용
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /*
     * 처리 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SuggestionStatus status;

    /*
     * 작성일
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /*
     * 수정일
     */
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.status = SuggestionStatus.RECEIVED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateStatus(SuggestionStatus status) {
        this.status = status;
    }
}