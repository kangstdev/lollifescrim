<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>참여 관리</title>
    <link rel="stylesheet" href="/css/notice/notice.css">
    <link rel="stylesheet" href="<c:url value='/css/common/mobile.css' />">
</head>
<body>
<div class="notice-page">
    <div class="notice-container">

        <div class="notice-header">
            <div>
                <p class="notice-subtitle">관리자</p>
                <h1 class="notice-title">참여 관리</h1>
                <p class="notice-desc">
                    항목과 시간을 선택해서 참여자를 확인하고 강제로 취소할 수 있습니다.
                </p>
            </div>

            <div class="notice-actions">
                <a class="notice-btn secondary" href="/admin">관리자 홈</a>
                <a class="notice-btn secondary" href="/main">메인으로</a>
            </div>
        </div>

        <div class="notice-list-box" style="margin-bottom: 18px;">
            <form class="admin-filter-form" method="get" action="/admin/participations">

                <div class="admin-filter-group">
                    <label>참여 항목</label>
                    <select name="type" class="status-select">
                        <option value="">전체</option>

                        <c:forEach var="type" items="${participationTypes}">
                            <option value="${type}" ${type eq selectedType ? 'selected' : ''}>
                                ${type.label}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="admin-filter-group">
                    <label>시간</label>
                    <select name="time" class="status-select">
                        <option value="">전체</option>

                        <c:forEach var="time" items="${times}">
                            <option value="${time}" ${time eq selectedTime ? 'selected' : ''}>
                                ${time}시
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="admin-filter-actions">
                    <button type="submit" class="notice-btn primary">조회</button>
                    <a class="notice-btn secondary" href="/admin/participations">초기화</a>
                </div>

            </form>
        </div>

        <div class="notice-list-box">
            <c:choose>
                <c:when test="${empty participations}">
                    <div class="notice-empty">
                        <p class="notice-empty-title">조건에 맞는 참여 신청이 없습니다.</p>
                        <p class="notice-empty-desc">
                            다른 항목이나 시간을 선택해보세요.
                        </p>
                    </div>
                </c:when>

                <c:otherwise>
                    <c:forEach var="participation" items="${participations}">
                        <div class="admin-row">
                            <div class="admin-row-main">
                                <div class="admin-row-title">
                                    <c:out value="${participation.member.nickname}" />

                                    <span class="notice-badge">
                                        ${participation.participationType.label}
                                    </span>
                                </div>

                                <div class="admin-row-desc">
                                    시간 ${participation.selectedTimes}

                                    <c:if test="${not empty participation.selectedLine}">
                                        · 라인 ${participation.selectedLine}
                                    </c:if>

                                    <c:if test="${not empty participation.member.tier}">
                                        · 티어 ${participation.member.tier}
                                    </c:if>
                                </div>
                            </div>

                            <div class="admin-row-actions">
                                <form method="post"
                                      action="/admin/participations/${participation.id}/delete"
                                      onsubmit="return confirm('해당 참여 신청을 강제로 취소하시겠습니까?');">
                                    <button type="submit" class="notice-btn danger">
                                        강제 취소
                                    </button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

    </div>
</div>
</body>
</html>