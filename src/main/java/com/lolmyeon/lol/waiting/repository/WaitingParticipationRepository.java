package com.lolmyeon.lol.waiting.repository;

import com.lolmyeon.lol.member.entity.Member;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import com.lolmyeon.lol.waiting.entity.WaitingParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;
import java.util.Optional;

public interface WaitingParticipationRepository extends JpaRepository<WaitingParticipation, Long> {

    List<WaitingParticipation> findAllByParticipationTypeAndSelectedTimeOrderByCreatedAtAsc(
            ParticipationType participationType,
            String selectedTime
    );

    List<WaitingParticipation> findAllByMemberIdOrderByCreatedAtDesc(Long memberId);

    List<WaitingParticipation> findAllByMemberOrderByCreatedAtDesc(Member member);

    Optional<WaitingParticipation> findByMemberAndParticipationTypeAndSelectedTime(
            Member member,
            ParticipationType participationType,
            String selectedTime
    );

    @EntityGraph(attributePaths = "member")
    List<WaitingParticipation> findAllByParticipationTypeOrderBySelectedTimeAscCreatedAtAsc(
            ParticipationType participationType
    );


}

