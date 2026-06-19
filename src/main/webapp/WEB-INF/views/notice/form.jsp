<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>
        <c:choose>
            <c:when test="${empty notice}">공지 작성</c:when>
            <c:otherwise>공지 수정</c:otherwise>
        </c:choose>
    </title>
    <link rel="stylesheet" href="/css/notice/notice.css">
</head>
<body>

<div class="notice-page">

    <div class="notice-container">

        <div class="notice-header">
            <div>
                <p class="notice-subtitle">공지사항</p>

                <h1 class="notice-title">
                    <c:choose>
                        <c:when test="${empty notice}">공지 작성</c:when>
                        <c:otherwise>공지 수정</c:otherwise>
                    </c:choose>
                </h1>

                <p class="notice-desc">
                    내전 운영 공지와 사이트 안내 내용을 작성합니다.
                </p>
            </div>

            <div class="notice-actions">
                <a href="/notice/list" class="notice-btn secondary">목록으로</a>
            </div>
        </div>

        <c:choose>
            <c:when test="${empty notice}">
                <form action="/notice/write" method="post" class="notice-form">
            </c:when>
            <c:otherwise>
                <form action="/notice/${notice.id}/edit" method="post" class="notice-form">
            </c:otherwise>
        </c:choose>

            <div class="form-group">
                <label for="title">제목</label>
                <input
                        type="text"
                        id="title"
                        name="title"
                        value="${notice.title}"
                        placeholder="공지 제목을 입력해주세요."
                        required
                >
            </div>

            <div class="form-group">
                <label for="content">내용</label>
                <textarea
                        id="content"
                        name="content"
                        rows="12"
                        placeholder="공지 내용을 입력해주세요."
                        required>${notice.content}</textarea>
            </div>

            <div class="form-actions">
                <a href="/notice/list" class="notice-btn secondary">취소</a>

                <button type="submit" class="notice-btn primary">
                    <c:choose>
                        <c:when test="${empty notice}">등록</c:when>
                        <c:otherwise>수정 완료</c:otherwise>
                    </c:choose>
                </button>
            </div>

        </form>

    </div>

</div>

</body>
</html>