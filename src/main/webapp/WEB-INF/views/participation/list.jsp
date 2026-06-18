<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>${participationType.label} 신청 현황</title>

    <link rel="stylesheet" href="<c:url value='/css/participation/list.css' />">
</head>
<body>

<div class="page-wrap">
    <div class="status-card">

        <div class="status-header">
            <div>
                <h1>${participationType.label} 신청 현황</h1>
                <p>시간별 · 라인별 신청자를 한눈에 확인할 수 있습니다.</p>
            </div>

            <a href="/participation/list?type=${participationType}&time=${selectedTime}" class="refresh-btn">
                ↻
            </a>
        </div>

        <div class="tab-row">
            <c:forEach var="time" items="${times}">
                <a
                    href="/participation/list?type=${participationType}&time=${time}"
                    class="time-tab ${selectedTime == time ? 'active' : ''}"
                >
                    <c:choose>
                        <c:when test="${time == '20'}">오후 8시</c:when>
                        <c:when test="${time == '21'}">오후 9시</c:when>
                        <c:when test="${time == '22'}">오후 10시</c:when>
                        <c:otherwise>${time}시</c:otherwise>
                    </c:choose>
                </a>
            </c:forEach>
        </div>

        <c:choose>
            <c:when test="${participationType.name() == 'ARAM'}">

                <div class="line-section">
                    <div class="line-title">
                        <span>증바람</span>

                        <c:set var="aramCount" value="0" />

                        <c:forEach var="participation" items="${participations}">
                            <c:forEach var="participationTime" items="${fn:split(participation.selectedTimes, ',')}">
                                <c:if test="${participationTime == selectedTime}">
                                    <c:set var="aramCount" value="${aramCount + 1}" />
                                </c:if>
                            </c:forEach>
                        </c:forEach>

                        <strong>${aramCount}명</strong>
                    </div>

                    <div class="member-list">
                        <c:set var="hasAramMember" value="false" />

                        <c:forEach var="participation" items="${participations}">
                            <c:forEach var="participationTime" items="${fn:split(participation.selectedTimes, ',')}">
                                <c:if test="${participationTime == selectedTime}">
                                    <c:set var="hasAramMember" value="true" />

                                    <div class="member-row">
                                        <span class="nickname">${participation.member.nickname}</span>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </c:forEach>

                        <c:if test="${hasAramMember == false}">
                            <div class="empty-text">참여자 없음</div>
                        </c:if>
                    </div>
                </div>

            </c:when>

            <c:otherwise>

                <c:forEach var="line" items="${lines}">
                    <c:set var="lineCount" value="0" />

                    <c:forEach var="participation" items="${participations}">
                        <c:if test="${not empty participation.selectedLine && participation.selectedLine.name() == line.name()}">
                            <c:forEach var="participationTime" items="${fn:split(participation.selectedTimes, ',')}">
                                <c:if test="${participationTime == selectedTime}">
                                    <c:set var="lineCount" value="${lineCount + 1}" />
                                </c:if>
                            </c:forEach>
                        </c:if>
                    </c:forEach>

                    <div class="line-section">
                        <div class="line-title">
                            <span>
                                <c:choose>
                                    <c:when test="${line.name() == 'TOP'}">탑</c:when>
                                    <c:when test="${line.name() == 'JUNGLE'}">정글</c:when>
                                    <c:when test="${line.name() == 'MID'}">미드</c:when>
                                    <c:when test="${line.name() == 'AD'}">원딜</c:when>
                                    <c:when test="${line.name() == 'SUPPORT'}">서포터</c:when>
                                    <c:otherwise>${line}</c:otherwise>
                                </c:choose>
                            </span>



                            <strong>${lineCount}명</strong>
                        </div>

                        <div class="member-list">
                            <c:set var="hasMember" value="false" />

                            <c:forEach var="participation" items="${participations}">
                                <c:if test="${not empty participation.selectedLine && participation.selectedLine.name() == line.name()}">
                                    <c:forEach var="participationTime" items="${fn:split(participation.selectedTimes, ',')}">
                                        <c:if test="${participationTime == selectedTime}">
                                            <c:set var="hasMember" value="true" />

                                            <div class="member-row">
                                                <span class="nickname">${participation.member.nickname}</span>

                                                <div class="member-lines">
                                                    <span class="line-chip">
                                                        <c:choose>
                                                            <c:when test="${participation.member.mainLine.name() == 'TOP'}">탑</c:when>
                                                            <c:when test="${participation.member.mainLine.name() == 'JUNGLE'}">정글</c:when>
                                                            <c:when test="${participation.member.mainLine.name() == 'MID'}">미드</c:when>
                                                            <c:when test="${participation.member.mainLine.name() == 'AD'}">원딜</c:when>
                                                            <c:when test="${participation.member.mainLine.name() == 'SUPPORT'}">서포터</c:when>
                                                            <c:otherwise>${participation.member.mainLine}</c:otherwise>
                                                        </c:choose>
                                                    </span>

                                                    <span class="line-chip sub">
                                                        <c:choose>
                                                            <c:when test="${participation.member.subLine.name() == 'TOP'}">탑</c:when>
                                                            <c:when test="${participation.member.subLine.name() == 'JUNGLE'}">정글</c:when>
                                                            <c:when test="${participation.member.subLine.name() == 'MID'}">미드</c:when>
                                                            <c:when test="${participation.member.subLine.name() == 'AD'}">원딜</c:when>
                                                            <c:when test="${participation.member.subLine.name() == 'SUPPORT'}">서포터</c:when>
                                                            <c:otherwise>${participation.member.subLine}</c:otherwise>
                                                        </c:choose>
                                                    </span>

                                                    <span class="line-chip">
                                                        ${participation.member.tier}
                                                    </span>
                                                </div>

                                                <span class="selected-line">
                                                    <c:choose>
                                                        <c:when test="${participation.selectedLine.name() == 'TOP'}">탑 신청</c:when>
                                                        <c:when test="${participation.selectedLine.name() == 'JUNGLE'}">정글 신청</c:when>
                                                        <c:when test="${participation.selectedLine.name() == 'MID'}">미드 신청</c:when>
                                                        <c:when test="${participation.selectedLine.name() == 'AD'}">원딜 신청</c:when>
                                                        <c:when test="${participation.selectedLine.name() == 'SUPPORT'}">서포터 신청</c:when>
                                                        <c:otherwise>${participation.selectedLine}</c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                            </c:forEach>

                            <c:if test="${hasMember == false}">
                                <div class="empty-text">참여자 없음</div>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>

            </c:otherwise>
        </c:choose>

        <div class="bottom-actions">
            <a href="/main" class="back-btn">메인으로 돌아가기</a>
            <a href="/participation/form?type=${participationType}" class="join-btn">신청하러 가기</a>
        </div>

    </div>
</div>

</body>
</html>