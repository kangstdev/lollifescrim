<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>참여 신청</title>

    <link rel="stylesheet" href="<c:url value='/css/participation/form.css' />">
</head>
<body>

<div class="page-wrap">
    <div class="form-card">

        <div class="header">
            <div class="badge">
                <c:choose>
                    <c:when test="${participationType.name() == 'NORMAL'}">🔥</c:when>
                    <c:when test="${participationType.name() == 'FREE'}">🌀</c:when>
                    <c:when test="${participationType.name() == 'FLEX'}">🏆</c:when>
                    <c:when test="${participationType.name() == 'ARAM'}">❄️</c:when>
                    <c:otherwise>⚔️</c:otherwise>
                </c:choose>
            </div>

            <h1>${participationType.label} 참여 신청</h1>

            <c:choose>
                <c:when test="${participationType.name() == 'ARAM'}">
                    <p>참여 가능한 시간을 선택해주세요.</p>
                </c:when>
                <c:otherwise>
                    <p>참여 라인과 가능한 시간을 선택해주세요.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="profile-box">
            <p>
                닉네임 <strong>${loginMember.nickname}</strong>
            </p>
            <p>
                주라인 <strong>${loginMember.mainLine}</strong>
                · 부라인 <strong>${loginMember.subLine}</strong>
            </p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="error-box">
                ${errorMessage}
            </div>
        </c:if>

        <form action="/participation" method="post" id="participationForm">
            <input type="hidden" name="participationType" value="${participationType}">

            <c:if test="${participationType.name() != 'ARAM'}">
                <div class="form-group">
                    <label class="form-label">참여 라인</label>

                    <div class="line-grid">
                        <c:forEach var="line" items="${lines}">
                            <label class="option-card">
                                <input
                                        type="radio"
                                        name="selectedLine"
                                        value="${line.name()}"
                                        data-line-name="${line.name()}"
                                        required
                                >

                                <span>
                                    <c:choose>
                                        <c:when test="${line.name() == 'TOP'}">탑</c:when>
                                        <c:when test="${line.name() == 'JUNGLE'}">정글</c:when>
                                        <c:when test="${line.name() == 'MID'}">미드</c:when>
                                        <c:when test="${line.name() == 'AD'}">원딜</c:when>
                                        <c:when test="${line.name() == 'ADC'}">원딜</c:when>
                                        <c:when test="${line.name() == 'SUPPORT'}">서폿</c:when>
                                        <c:otherwise>${line}</c:otherwise>
                                    </c:choose>
                                </span>
                            </label>
                        </c:forEach>
                    </div>
                </div>
            </c:if>

            <div class="form-group">
                <label class="form-label">참여 가능 시간</label>

                <div class="time-section">
                    <p class="time-section-title">고정 시간</p>

                    <div class="fixed-time-grid" id="timeGrid">
                        <c:forEach var="time" items="${times}">
                            <label class="option-card time-option" data-time="${time}">
                                <input
                                        type="checkbox"
                                        name="selectedTimes"
                                        value="${time}"
                                        class="time-checkbox"
                                        data-time="${time}"
                                >
                                <span class="time-label">${time}시</span>
                            </label>
                        </c:forEach>
                    </div>
                </div>

                <div class="time-section">
                    <p class="time-section-title">사용자 설정 시간</p>

                    <c:choose>
                        <c:when test="${empty partyTimes}">
                            <div class="empty-time-box">
                                아직 만들어진 파티 시간이 없습니다.
                            </div>
                        </c:when>

                        <c:otherwise>
                            <div class="custom-party-time-grid">
                                <c:forEach var="time" items="${partyTimes}">
                                    <label class="option-card time-option" data-time="${time}">
                                        <input
                                                type="checkbox"
                                                name="selectedTimes"
                                                value="${time}"
                                                class="time-checkbox"
                                                data-time="${time}"
                                        >
                                        <span class="time-label">${time}시</span>
                                    </label>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <p class="time-help">
                    고정 시간 또는 파티장이 만든 시간을 선택해주세요.
                </p>
            </div>

            <div class="form-group">
                <label class="form-label">현재 선택한 시간 현황</label>

                <div class="current-status-box" id="currentStatusBox">
                    <p class="status-empty-text">
                        시간을 선택하면 해당 시간의 참여 현황이 표시됩니다.
                    </p>
                </div>
            </div>

            <div class="button-row">
                <a href="/main" class="back-btn">메인으로</a>
                <button type="submit" class="submit-btn">참여 신청</button>
            </div>
        </form>

        <c:if test="${participationType.name() != 'ARAM'}">
            <div class="notice">
                일반 내전은 가입 시 설정한 주라인/부라인만 선택 가능합니다.
            </div>
        </c:if>

    </div>
</div>

<script>
    const occupiedSlotKeys = [
        <c:forEach var="key" items="${occupiedSlotKeys}" varStatus="status">
            "${key}"<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    const participationType = "${participationType.name()}";
</script>

<script src="<c:url value='/js/participation/form.js' />"></script>

</body>
</html>