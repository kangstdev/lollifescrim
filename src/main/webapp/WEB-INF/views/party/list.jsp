<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>파티 목록</title>

    <link rel="stylesheet" href="<c:url value='/css/party/list.css' />">
    <link rel="stylesheet" href="<c:url value='/css/common/mobile.css' />">
</head>
<body>

<div class="page-wrap">
    <div class="list-card">

        <div class="header">
            <div class="badge">👥</div>
            <h1>파티 목록</h1>
            <p>만들어진 파티를 확인하고 참여할 수 있습니다.</p>
        </div>

        <div class="profile-box">
            <p>
                로그인 사용자 <strong>${loginMember.nickname}</strong>
            </p>
            <p>
                주라인 <strong>${loginMember.mainLine}</strong>
                · 부라인 <strong>${loginMember.subLine}</strong>
            </p>
        </div>

        <div class="filter-row">
            <a href="/party/list"
               class="filter-btn ${empty selectedType ? 'active' : ''}">
                전체
            </a>

            <c:forEach var="type" items="${participationTypes}">
                <a href="/party/list?type=${type.name()}"
                   class="filter-btn ${selectedType == type ? 'active' : ''}">
                    <c:choose>
                        <c:when test="${type.name() == 'NORMAL'}">내전</c:when>
                        <c:when test="${type.name() == 'FREE'}">자유내전</c:when>
                        <c:when test="${type.name() == 'FLEX'}">자유랭크</c:when>
                        <c:when test="${type.name() == 'ARAM'}">증바람</c:when>
                        <c:otherwise>${type}</c:otherwise>
                    </c:choose>
                </a>
            </c:forEach>
        </div>

        <div class="top-actions">
            <a href="/main" class="back-btn">메인으로</a>
            <a href="/party/form" class="create-btn">파티 만들기</a>
        </div>

        <c:choose>
            <c:when test="${empty parties}">
                <div class="empty-box">
                    <div class="empty-icon">🕹️</div>
                    <p>아직 만들어진 파티가 없습니다.</p>
                    <a href="/party/form">첫 파티 만들기</a>
                </div>
            </c:when>

            <c:otherwise>
                <div class="party-list">
                    <c:forEach var="party" items="${parties}">
                        <div class="party-card">

                            <div class="party-top">
                                <div>
                                    <span class="type-badge">
                                        <c:choose>
                                            <c:when test="${party.participationType.name() == 'NORMAL'}">🔥 내전</c:when>
                                            <c:when test="${party.participationType.name() == 'FREE'}">🌀 자유내전</c:when>
                                            <c:when test="${party.participationType.name() == 'FLEX'}">🏆 자유랭크</c:when>
                                            <c:when test="${party.participationType.name() == 'ARAM'}">❄️ 증바람</c:when>
                                            <c:otherwise>${party.participationType}</c:otherwise>
                                        </c:choose>
                                    </span>

                                    <h2>${party.title}</h2>
                                </div>

                                <div class="time-badge">
                                    ${party.partyTime}시
                                </div>
                            </div>

                            <div class="party-info">
                                <p>
                                    만든 사람
                                    <strong>${party.creator.nickname}</strong>
                                </p>

                                <p>
                                    생성일
                                    <span>${party.createdAt}</span>
                                </p>
                            </div>

                            <c:if test="${not empty party.memo}">
                                <div class="memo-box">
                                    ${party.memo}
                                </div>
                            </c:if>

                            <div class="party-actions">
                                <a href="/participation/form?type=${party.participationType.name()}" class="join-btn">
                                    이 종류로 참여 신청
                                </a>
                            </div>

                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="notice">
            현재는 파티 목록 확인과 해당 종류 신청 화면 이동까지만 연결되어 있습니다.
        </div>

    </div>
</div>

</body>
</html>