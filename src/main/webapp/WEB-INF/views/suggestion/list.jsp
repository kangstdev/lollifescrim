<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>건의사항</title>
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
                    사이트 기능 개선 의견을 남길 수 있습니다.
                </p>
            </div>

            <div class="notice-actions">
                <a class="notice-btn secondary" href="/main">메인으로</a>
                <a class="notice-btn primary" href="/suggestion/write">건의 작성</a>
            </div>
        </div>

        <div class="notice-tab-box">
            <a class="notice-tab" href="/notice/list">공지사항</a>
            <a class="notice-tab active" href="/suggestion/list">건의사항</a>
        </div>

        <div class="notice-list-box">
            <c:choose>
                <c:when test="${empty suggestions}">
                    <div class="notice-empty">
                        <p class="notice-empty-title">등록된 건의사항이 없습니다.</p>
                        <p class="notice-empty-desc">
                            사이트에 필요한 기능이나 개선 의견을 남겨주세요.
                        </p>
                    </div>
                </c:when>

                <c:otherwise>
                    <c:forEach var="suggestion" items="${suggestions}">
                        <a class="notice-item" href="/suggestion/${suggestion.id}">
                            <div class="notice-item-main">
                                <span class="notice-badge">
                                    ${suggestion.status.label}
                                </span>
                                <span class="notice-item-title">
                                    <c:out value="${suggestion.title}" />
                                </span>
                            </div>

                            <div class="notice-item-meta">
                                <span>
                                    <c:out value="${suggestion.writer.nickname}" />
                                </span>
                                <span>
                                    ${suggestion.createdAt}
                                </span>
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