<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${suggestion.title}</title>
    <link rel="stylesheet" href="/css/notice/notice.css">
    <link rel="stylesheet" href="<c:url value='/css/common/mobile.css' />">
</head>
<body>
<div class="notice-page">
    <div class="notice-container">

        <div class="notice-header">
            <div>
                <p class="notice-subtitle">건의사항</p>
                <h1 class="notice-title">
                    <c:out value="${suggestion.title}" />
                </h1>
                <p class="notice-desc">
                    작성자:
                    <c:out value="${suggestion.writer.nickname}" />
                    · 상태: ${suggestion.status.label}
                    · 작성일: ${suggestion.createdAt}
                </p>
            </div>

            <div class="notice-actions">
                <a class="notice-btn secondary" href="/suggestion/list">목록으로</a>

                <c:if test="${loginMember.role eq 'ADMIN' or loginMember.id eq suggestion.writer.id}">
                    <a class="notice-btn primary" href="/suggestion/${suggestion.id}/edit">수정</a>

                    <form class="inline-form"
                          action="/suggestion/${suggestion.id}/delete"
                          method="post"
                          onsubmit="return confirm('건의사항을 삭제하시겠습니까?');">
                        <button type="submit" class="notice-btn danger">삭제</button>
                    </form>
                </c:if>
            </div>
        </div>

        <div class="notice-tab-box">
            <a class="notice-tab" href="/notice/list">공지사항</a>
            <a class="notice-tab active" href="/suggestion/list">건의사항</a>
        </div>

        <c:if test="${loginMember.role eq 'ADMIN'}">
            <div class="notice-list-box" style="margin-bottom: 18px;">
                <form action="/suggestion/${suggestion.id}/status" method="post" class="status-form">
                    <label class="status-label">처리 상태 변경</label>

                    <select name="status" class="status-select">
                        <c:forEach var="status" items="${statuses}">
                            <option value="${status}" ${status eq suggestion.status ? 'selected' : ''}>
                                ${status.label}
                            </option>
                        </c:forEach>
                    </select>

                    <button type="submit" class="notice-btn primary">변경</button>
                </form>
            </div>
        </c:if>

        <div class="notice-detail-box">
            <div class="notice-detail-content">
                <c:out value="${suggestion.content}" />
            </div>
        </div>

    </div>
</div>
</body>
</html>