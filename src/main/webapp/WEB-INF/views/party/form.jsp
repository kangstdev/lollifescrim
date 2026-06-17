<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>파티 만들기</title>

    <link rel="stylesheet" href="<c:url value='/css/party/form.css' />">
</head>
<body>

<div class="page-wrap">
    <div class="form-card">

        <div class="header">
            <div class="badge">➕</div>
            <h1>파티 만들기</h1>
            <p>원하는 시간과 종류를 선택해서 새 파티를 만들어보세요.</p>
        </div>

        <div class="profile-box">
            <p>
                만든 사람 <strong>${loginMember.nickname}</strong>
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

        <form action="/party" method="post">

            <div class="form-group">
                <label class="form-label">파티 종류</label>

                <div class="type-grid">
                    <c:forEach var="type" items="${participationTypes}">
                        <label class="option-card">
                            <input
                                    type="radio"
                                    name="participationType"
                                    value="${type.name()}"
                                    required
                                    <c:if test="${selectedParticipationType == type}">checked</c:if>
                            >

                            <span>
                                <c:choose>
                                    <c:when test="${type.name() == 'NORMAL'}">🔥 내전</c:when>
                                    <c:when test="${type.name() == 'FREE'}">🌀 자유내전</c:when>
                                    <c:when test="${type.name() == 'FLEX'}">🏆 자유랭크</c:when>
                                    <c:when test="${type.name() == 'ARAM'}">❄️ 증바람</c:when>
                                    <c:otherwise>${type}</c:otherwise>
                                </c:choose>
                            </span>
                        </label>
                    </c:forEach>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">파티 시간</label>

                <input
                        type="number"
                        name="partyTime"
                        class="text-input"
                        min="0"
                        max="23"
                        placeholder="예: 19"
                        value="${partyTime}"
                        required
                >

                <p class="help-text">
                    숫자만 입력해주세요. 예: 19 → 19시
                </p>
            </div>

            <div class="form-group">
                <label class="form-label">파티 제목</label>

                <input
                        type="text"
                        name="title"
                        class="text-input"
                        maxlength="100"
                        placeholder="예: 19시 자유내전 구합니다"
                        value="${title}"
                        required
                >
            </div>

            <div class="form-group">
                <label class="form-label">메모</label>

                <textarea
                        name="memo"
                        class="memo"
                        placeholder="전달할 내용이 있으면 적어주세요. 예: 즐겜 위주, 마이크 가능"
                >${memo}</textarea>
            </div>

            <div class="button-row">
                <a href="/main" class="back-btn">메인으로</a>
                <button type="submit" class="submit-btn">파티 만들기</button>
            </div>
        </form>

        <div class="notice">
            만든 파티는 파티 목록에서 다른 사람들이 확인할 수 있습니다.
        </div>

    </div>
</div>

</body>
</html>