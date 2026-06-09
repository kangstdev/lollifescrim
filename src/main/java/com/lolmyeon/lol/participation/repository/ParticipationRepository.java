package com.lolmyeon.lol.participation.repository;

import com.lolmyeon.lol.member.entity.Member;
import com.lolmyeon.lol.participation.entity.Participation;
import com.lolmyeon.lol.participation.entity.ParticipationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    Optional<Participation> findByMemberAndParticipationType(Member member, ParticipationType participationType);

    List<Participation> findAllByParticipationTypeOrderByCreatedAtDesc(ParticipationType participationType);

    List<Participation> findAllByMemberOrderByCreatedAtDesc(Member member);
}