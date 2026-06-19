<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>${notice.title}</title>
    <link rel="stylesheet" href="/css/notice/notice.css">
</head>
<body>

<div class="notice-page">

    <div class="notice-container">

        <div class="notice-header">
            <div>
                <p class="notice-subtitle">공지사항</p>
                <h1 class="notice-title">${notice.title}</h1>
                <p class="notice-desc">
                    작성자 ${notice.writer.nickname} · ${notice.createdAt}
                </p>
            </div>

            <div class="notice-actions">
                <a href="/notice/list" class="notice-btn secondary">목록으로</a>

                <c:if test="${loginMember.role == 'ADMIN'}">
                    <a href="/notice/${notice.id}/edit" class="notice-btn primary">수정</a>

                    <form action="/notice/${notice.id}/delete" method="post" class="inline-form"
                          onsubmit="return confirm('공지사항을 삭제하시겠습니까?');">
                        <button type="submit" class="notice-btn danger">삭제</button>
                    </form>
                </c:if>
            </div>
        </div>

        <div class="notice-detail-box">
            <div class="notice-detail-content">
                <c:out value="${notice.content}" />
            </div>
        </div>

    </div>

</div>

</body>
</html>