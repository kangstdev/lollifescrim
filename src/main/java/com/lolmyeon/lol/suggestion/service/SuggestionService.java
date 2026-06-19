package com.lolmyeon.lol.suggestion.service;

import com.lolmyeon.lol.member.entity.Member;
import com.lolmyeon.lol.member.repository.MemberRepository;
import com.lolmyeon.lol.suggestion.entity.Suggestion;
import com.lolmyeon.lol.suggestion.entity.SuggestionStatus;
import com.lolmyeon.lol.suggestion.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SuggestionService {

    private final SuggestionRepository suggestionRepository;
    private final MemberRepository memberRepository;

    /*
     * 건의사항 목록 조회
     */
    @Transactional(readOnly = true)
    public List<Suggestion> findAllSuggestions() {
        return suggestionRepository.findAllWithWriterOrderByCreatedAtDesc();
    }

    /*
     * 건의사항 상세 조회
     */
    @Transactional(readOnly = true)
    public Suggestion findSuggestion(Long suggestionId) {
        return suggestionRepository.findByIdWithWriter(suggestionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 건의사항입니다."));
    }

    /*
     * 건의사항 작성
     */
    public void createSuggestion(Long writerId, String title, String content) {
        Member writer = memberRepository.findById(writerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Suggestion suggestion = Suggestion.builder()
                .writer(writer)
                .title(title)
                .content(content)
                .build();

        suggestionRepository.save(suggestion);
    }

    /*
     * 건의사항 수정
     */
    public void updateSuggestion(Long suggestionId, Long loginMemberId, boolean admin, String title, String content) {
        Suggestion suggestion = suggestionRepository.findByIdWithWriter(suggestionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 건의사항입니다."));

        if (!admin && !suggestion.getWriter().getId().equals(loginMemberId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        suggestion.update(title, content);
    }

    /*
     * 건의사항 상태 변경
     *
     * 관리자만 사용합니다.
     */
    public void updateStatus(Long suggestionId, SuggestionStatus status) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 건의사항입니다."));

        suggestion.updateStatus(status);
    }

    /*
     * 건의사항 삭제
     *
     * 작성자 본인 또는 관리자만 삭제 가능합니다.
     */
    public void deleteSuggestion(Long suggestionId, Long loginMemberId, boolean admin) {
        Suggestion suggestion = suggestionRepository.findByIdWithWriter(suggestionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 건의사항입니다."));

        if (!admin && !suggestion.getWriter().getId().equals(loginMemberId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        suggestionRepository.delete(suggestion);
    }
}