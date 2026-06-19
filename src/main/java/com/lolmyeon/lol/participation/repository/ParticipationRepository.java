package com.lolmyeon.lol.participation.repository;

import com.lolmyeon.lol.common.LolLine;
import com.lolmyeon.lol.member.entity.Member;
import com.lolmyeon.lol.participation.entity.Participation;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    Optional<Participation> findByMemberAndParticipationType(Member member, ParticipationType participationType);

    @EntityGraph(attributePaths = "member")
    List<Participation> findAllByParticipationTypeOrderByCreatedAtDesc(ParticipationType participationType);

    List<Participation> findAllByMemberOrderByCreatedAtDesc(Member member);

    // 같은 참여 종류 + 같은 라인으로 신청된 목록 조회
    // 중복 신청 방지에 사용
    List<Participation> findAllByParticipationTypeAndSelectedLine(
            ParticipationType participationType,
            LolLine selectedLine
    );

    List<Participation> findAllByMemberId(Long memberId);

    @Query("""
        select p
        from Participation p
        join fetch p.member
        order by p.createdAt desc
        """)
    List<Participation> findAllWithMemberOrderByCreatedAtDesc();
}