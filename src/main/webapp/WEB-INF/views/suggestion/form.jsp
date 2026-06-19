<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">

    <c:choose>
        <c:when test="${empty suggestion}">
            <title>건의사항 작성</title>
        </c:when>
        <c:otherwise>
            <title>건의사항 수정</title>
        </c:otherwise>
    </c:choose>

    <link rel="stylesheet" href="/css/notice/notice.css">
</head>
<body>
<div class="notice-page">
    <div class="notice-container">

        <div class="notice-header">
            <div>
                <p class="notice-subtitle">건의사항</p>

                <c:choose>
                    <c:when test="${empty suggestion}">
                        <h1 class="notice-title">건의사항 작성</h1>
                    </c:when>
                    <c:otherwise>
                        <h1 class="notice-title">건의사항 수정</h1>
                    </c:otherwise>
                </c:choose>

                <p class="notice-desc">
                    사이트 기능 개선 의견만 작성해주세요.
                </p>
            </div>

            <div class="notice-actions">
                <a class="notice-btn secondary" href="/suggestion/list">목록으로</a>
            </div>
        </div>

        <div class="notice-tab-box">
            <a class="notice-tab" href="/notice/list">공지사항</a>
            <a class="notice-tab active" href="/suggestion/list">건의사항</a>
        </div>

        <form class="notice-form"
              method="post"
              action="${empty suggestion ? '/suggestion/write' : '/suggestion/'.concat(suggestion.id).concat('/edit')}">

            <div class="form-group">
                <label for="title">제목</label>
                <input type="text"
                       id="title"
                       name="title"
                       maxlength="100"
                       required
                       value="<c:out value='${suggestion.title}' />"
                       placeholder="예) 참여 현황에서 라인별 필터가 있으면 좋겠습니다.">
            </div>

            <div class="form-group">
                <label for="content">내용</label>
                <textarea id="content"
                          name="content"
                          rows="12"
                          required
                          placeholder="어떤 기능이 필요하고, 왜 필요한지 간단히 적어주세요."><c:out value="${suggestion.content}" /></textarea>
            </div>

            <div class="form-actions">
                <a class="notice-btn secondary" href="/suggestion/list">취소</a>

                <button type="submit" class="notice-btn primary">
                    <c:choose>
                        <c:when test="${empty suggestion}">
                            등록
                        </c:when>
                        <c:otherwise>
                            수정 완료
                        </c:otherwise>
                    </c:choose>
                </button>
            </div>
        </form>

    </div>
</div>
</body>
</html>