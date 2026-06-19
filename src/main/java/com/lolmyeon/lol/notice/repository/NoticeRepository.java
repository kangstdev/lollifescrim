package com.lolmyeon.lol.notice.repository;

import com.lolmyeon.lol.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    /*
     * 공지사항 목록 조회
     * writer까지 같이 조회해서 JSP에서 writer.nickname을 사용할 수 있게 합니다.
     */
    @Query("""
            select n
            from Notice n
            join fetch n.writer
            order by n.createdAt desc
            """)
    List<Notice> findAllWithWriterOrderByCreatedAtDesc();

    /*
     * 공지사항 상세 조회
     * 상세 화면에서도 writer.nickname을 사용할 수 있으므로 writer까지 같이 조회합니다.
     */
    @Query("""
            select n
            from Notice n
            join fetch n.writer
            where n.id = :noticeId
            """)
    Optional<Notice> findByIdWithWriter(Long noticeId);
}