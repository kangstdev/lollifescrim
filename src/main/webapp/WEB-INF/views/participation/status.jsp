<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>롤면서살자 | 참여 현황</title>

    <link rel="stylesheet" href="<c:url value='/css/participation/status.css' />">
    <link rel="stylesheet" href="<c:url value='/css/common/mobile.css' />">
</head>
<body>

<div class="page-wrap">
    <div class="status-card">

        <div class="top-nav">
            <a class="back-link" href="/main">← 메인으로</a>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="error-message">
                ${errorMessage}
            </div>
        </c:if>

        <h1>내 참여 현황</h1>
        <p class="subtitle">${loginMember.nickname}님의 현재 참여 신청 목록입니다.</p>

        <c:choose>
            <c:when test="${empty participations}">
                <div class="empty">
                    아직 참여 신청 내역이 없습니다.
                </div>
            </c:when>

            <c:otherwise>
                <div class="list">
                    <c:forEach var="p" items="${participations}">
                        <div class="item">

                            <div class="item-head">
                                <div class="type">${p.participationType.label}</div>

                                <div class="badge-row">
                                    <div class="badge">신청 완료</div>

                                    <form action="/participation/cancel"
                                          method="post"
                                          class="cancel-form"
                                          onsubmit="return confirm('참여 신청을 취소하시겠습니까?');">
                                        <input type="hidden" name="participationId" value="${p.id}">
                                        <button type="submit" class="cancel-badge">신청 취소</button>
                                    </form>
                                </div>
                            </div>

                            <p class="meta">
                                <strong>선택 라인</strong> :
                                <c:choose>
                                    <c:when test="${empty p.selectedLine}">
                                        없음
                                    </c:when>
                                    <c:when test="${p.selectedLine.name() == 'TOP'}">
                                        탑
                                    </c:when>
                                    <c:when test="${p.selectedLine.name() == 'JUNGLE'}">
                                        정글
                                    </c:when>
                                    <c:when test="${p.selectedLine.name() == 'MID'}">
                                        미드
                                    </c:when>
                                    <c:when test="${p.selectedLine.name() == 'AD'}">
                                        원딜
                                    </c:when>
                                    <c:when test="${p.selectedLine.name() == 'SUPPORT'}">
                                        서폿
                                    </c:when>
                                    <c:otherwise>
                                        ${p.selectedLine}
                                    </c:otherwise>
                                </c:choose>
                            </p>

                            <p class="meta">
                                <strong>참여신청시간</strong> :
                                ${fn:replace(fn:replace(fn:replace(p.selectedTimes, '20', '오후 8시'), '21', '오후 9시'), '22', '오후 10시')}
                            </p>

                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="quick-links">
            <a class="primary" href="/participation/form?type=NORMAL">내전 참여</a>
            <a href="/participation/form?type=FLEX">자유랭크 참여</a>
        </div>

    </div>
</div>

</body>
</html>