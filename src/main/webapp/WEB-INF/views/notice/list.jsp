<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>공지 및 건의사항</title>
    <link rel="stylesheet" href="/css/notice/notice.css">
</head>
<body>

<div class="notice-page">

    <div class="notice-container">

        <div class="notice-header">
            <div>
                <p class="notice-subtitle">롤면서살자</p>
                <h1 class="notice-title">공지 및 건의사항</h1>
                <p class="notice-desc">
                    내전 운영 공지와 사이트 개선 내용을 확인할 수 있습니다.
                </p>
            </div>

            <div class="notice-actions">
                <a href="/main" class="notice-btn secondary">메인으로</a>

                <c:if test="${loginMember.role == 'ADMIN'}">
                    <a href="/notice/write" class="notice-btn primary">공지 작성</a>
                </c:if>
            </div>
        </div>

        <div class="notice-tab-box">
            <a href="/notice/list" class="notice-tab active">공지사항</a>
            <a href="/suggestion/list" class="notice-tab">건의사항</a>
        </div>

        <div class="notice-list-box">

            <c:choose>
                <c:when test="${empty notices}">
                    <div class="notice-empty">
                        <p class="notice-empty-title">등록된 공지사항이 없습니다.</p>
                        <p class="notice-empty-desc">관리자가 공지사항을 작성하면 이곳에 표시됩니다.</p>
                    </div>
                </c:when>

                <c:otherwise>
                    <c:forEach var="notice" items="${notices}">
                        <a href="/notice/${notice.id}" class="notice-item">
                            <div class="notice-item-main">
                                <span class="notice-badge">공지</span>
                                <span class="notice-item-title">${notice.title}</span>
                            </div>

                            <div class="notice-item-meta">
                                <span>${notice.writer.nickname}</span>
                                <span>${notice.createdAt}</span>
                            </div>
                        </a>
                    </c:forEach>
                </c:otherwise>
            </c:choose>

        </div>

    </div>

</div>

</body>
</html>