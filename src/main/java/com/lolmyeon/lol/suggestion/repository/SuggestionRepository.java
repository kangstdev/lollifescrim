package com.lolmyeon.lol.suggestion.repository;

import com.lolmyeon.lol.suggestion.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {

    /*
     * 건의사항 목록 조회
     * writer까지 같이 조회해서 JSP에서 writer.nickname 사용 가능하게 합니다.
     */
    @Query("""
            select s
            from Suggestion s
            join fetch s.writer
            order by s.createdAt desc
            """)
    List<Suggestion> findAllWithWriterOrderByCreatedAtDesc();

    /*
     * 건의사항 상세 조회
     * 상세 화면에서도 writer.nickname을 사용하므로 writer까지 같이 조회합니다.
     */
    @Query("""
            select s
            from Suggestion s
            join fetch s.writer
            where s.id = :suggestionId
            """)
    Optional<Suggestion> findByIdWithWriter(Long suggestionId);
}