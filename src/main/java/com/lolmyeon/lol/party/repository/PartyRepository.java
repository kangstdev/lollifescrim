package com.lolmyeon.lol.party.repository;

import com.lolmyeon.lol.party.entiry.Party;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 파티 Repository
 *
 * Party 엔티티를 DB에서 조회/저장하는 역할입니다.
 *
 * 주의:
 * Party 안에 creator(Member)가 LAZY 로딩입니다.
 * 그래서 목록 화면에서 ${party.creator.nickname}을 쓰려면
 * 파티 조회할 때 creator도 같이 조회해야 합니다.
 */
public interface PartyRepository extends JpaRepository<Party, Long> {

    /**
     * 전체 파티 목록 조회
     *
     * join fetch p.creator:
     * - Party를 조회할 때 만든 사람 Member도 같이 조회합니다.
     * - 그래야 JSP에서 party.creator.nickname을 읽어도 오류가 안 납니다.
     */
    @Query("""
            select p
            from Party p
            join fetch p.creator
            order by p.createdAt desc
            """)
    List<Party> findAllByOrderByCreatedAtDesc();

    /**
     * 파티 종류별 목록 조회
     *
     * 예:
     * - NORMAL 파티만 조회
     * - FREE 파티만 조회
     * - FLEX 파티만 조회
     * - ARAM 파티만 조회
     */
    @Query("""
            select p
            from Party p
            join fetch p.creator
            where p.participationType = :participationType
            order by p.createdAt desc
            """)
    List<Party> findAllByParticipationTypeOrderByCreatedAtDesc(
            ParticipationType participationType
    );
}