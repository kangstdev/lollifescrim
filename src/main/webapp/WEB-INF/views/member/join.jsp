<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>롤면서살자 | 회원가입</title>

  <link rel="stylesheet" href="<c:url value='/css/member/join.css' />">
</head>
<body>

<div class="page-wrap">
    <div class="join-card">

        <div class="brand">
            <div class="brand-badge">🛡️</div>
            <h1>롤면서살자</h1>
            <p>내전 참여를 위한 모임원 등록</p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="error-message">
                ${errorMessage}
            </div>
        </c:if>

        <form:form action="/join" method="post" modelAttribute="joinRequest">

            <div class="form-group">
                <label for="nickname">닉네임</label>
                <form:input
                        path="nickname"
                        id="nickname"
                        cssClass="form-input"
                        placeholder="모임에서 사용하는 닉네임" />
                <form:errors path="nickname" cssClass="field-error" />
            </div>

            <div class="form-group">
                <label for="password">비밀번호</label>
                <form:password
                        path="password"
                        id="password"
                        cssClass="form-input"
                        placeholder="비밀번호 입력" />
                <form:errors path="password" cssClass="field-error" />
            </div>

            <div class="form-group">
                <label for="inviteCode">공식 인증번호</label>
                <form:input
                        path="inviteCode"
                        id="inviteCode"
                        cssClass="form-input"
                        placeholder="운영진에게 받은 인증번호" />
                <form:errors path="inviteCode" cssClass="field-error" />
            </div>

            <div class="line-help">
                <strong>일반 내전 안내</strong><br>
                일반 내전에서는 가입 시 선택한 주라인/부라인만 투표할 수 있습니다.
            </div>

            <div class="form-grid">
                <div class="form-group">
                    <label for="mainLine">주라인</label>
                    <form:select path="mainLine" id="mainLine" cssClass="form-input">
                        <form:option value="">선택</form:option>
                        <c:forEach var="line" items="${lines}">
                            <form:option value="${line}">
                                <c:choose>
                                    <c:when test="${line.name() == 'TOP'}">탑</c:when>
                                    <c:when test="${line.name() == 'JUNGLE'}">정글</c:when>
                                    <c:when test="${line.name() == 'MID'}">미드</c:when>
                                    <c:when test="${line.name() == 'AD'}">원딜</c:when>
                                    <c:when test="${line.name() == 'SUPPORT'}">서폿</c:when>
                                    <c:otherwise>${line}</c:otherwise>
                                </c:choose>
                            </form:option>
                        </c:forEach>
                    </form:select>
                    <form:errors path="mainLine" cssClass="field-error" />
                </div>

                <div class="form-group">
                    <label for="subLine">부라인</label>
                    <form:select path="subLine" id="subLine" cssClass="form-input">
                        <form:option value="">선택</form:option>
                        <c:forEach var="line" items="${lines}">
                            <form:option value="${line}">
                                <c:choose>
                                    <c:when test="${line.name() == 'TOP'}">탑</c:when>
                                    <c:when test="${line.name() == 'JUNGLE'}">정글</c:when>
                                    <c:when test="${line.name() == 'MID'}">미드</c:when>
                                    <c:when test="${line.name() == 'AD'}">원딜</c:when>
                                    <c:when test="${line.name() == 'SUPPORT'}">서폿</c:when>
                                    <c:otherwise>${line}</c:otherwise>
                                </c:choose>
                            </form:option>
                        </c:forEach>
                    </form:select>
                    <form:errors path="subLine" cssClass="field-error" />
                </div>
            </div>

            <button type="submit" class="submit-btn">가입하기</button>
        </form:form>

        <div class="bottom-link">
            이미 계정이 있나요?
            <a href="/login">로그인</a>
        </div>

    </div>
</div>

</body>
</html>