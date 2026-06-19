package com.lolmyeon.lol.notice.service;

import com.lolmyeon.lol.member.entity.Member;
import com.lolmyeon.lol.member.repository.MemberRepository;
import com.lolmyeon.lol.notice.entity.Notice;
import com.lolmyeon.lol.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    /*
     * 공지사항 목록 조회
     */
    @Transactional(readOnly = true)
    public List<Notice> findAllNotices() {
        return noticeRepository.findAllWithWriterOrderByCreatedAtDesc();
    }

    /*
     * 공지사항 상세 조회
     */
    @Transactional(readOnly = true)
    public Notice findNotice(Long noticeId) {
        return noticeRepository.findByIdWithWriter(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));
    }

    /*
     * 공지사항 작성
     */
    public void createNotice(Long writerId, String title, String content) {
        Member writer = memberRepository.findById(writerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Notice notice = Notice.builder()
                .writer(writer)
                .title(title)
                .content(content)
                .build();

        noticeRepository.save(notice);
    }

    /*
     * 공지사항 수정
     */
    public void updateNotice(Long noticeId, String title, String content) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));

        notice.update(title, content);
    }

    /*
     * 공지사항 삭제
     */
    public void deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));

        noticeRepository.delete(notice);
    }
}