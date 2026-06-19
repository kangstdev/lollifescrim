<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>내 정보 수정</title>

    <link rel="stylesheet" href="/css/member/edit.css">
</head>
<body>

<div class="page">

    <div class="edit-card">
        <h1>내 정보 수정</h1>
        <p class="description">
            닉네임, 비밀번호, 티어, 주라인, 부라인을 수정할 수 있습니다.
        </p>

        <c:if test="${not empty errorMessage}">
            <div class="error-box">
                ${errorMessage}
            </div>
        </c:if>

        <form:form method="post"
                   action="/member/edit"
                   modelAttribute="editMemberRequest"
                   class="edit-form">

            <div class="form-group">
                <label for="nickname">닉네임</label>
                <form:input path="nickname" id="nickname" cssClass="input"/>
                <form:errors path="nickname" cssClass="field-error"/>
            </div>

            <div class="form-group">
                <label for="password">새 비밀번호</label>
                <form:password path="password" id="password" cssClass="input"
                               placeholder="변경하지 않으려면 비워두세요"/>
                <form:errors path="password" cssClass="field-error"/>
            </div>

            <div class="form-group">
                <label for="tier">티어</label>
                <form:select path="tier" id="tier" cssClass="input">
                    <form:option value="">티어 선택</form:option>
                    <form:option value="IRON">아이언</form:option>
                    <form:option value="BRONZE">브론즈</form:option>
                    <form:option value="SILVER">실버</form:option>
                    <form:option value="GOLD">골드</form:option>
                    <form:option value="PLATINUM">플래티넘</form:option>
                    <form:option value="EMERALD">에메랄드</form:option>
                    <form:option value="DIAMOND">다이아몬드</form:option>
                    <form:option value="MASTER">마스터</form:option>
                    <form:option value="GRANDMASTER">그랜드마스터</form:option>
                    <form:option value="CHALLENGER">챌린저</form:option>
                </form:select>
                <form:errors path="tier" cssClass="field-error"/>
            </div>

            <div class="form-group">
                <label for="mainLine">주라인</label>
                <form:select path="mainLine" id="mainLine" cssClass="input">
                    <form:option value="">주라인 선택</form:option>
                    <c:forEach var="line" items="${lines}">
                        <form:option value="${line}">${line}</form:option>
                    </c:forEach>
                </form:select>
                <form:errors path="mainLine" cssClass="field-error"/>
            </div>

            <div class="form-group">
                <label for="subLine">부라인</label>
                <form:select path="subLine" id="subLine" cssClass="input">
                    <form:option value="">부라인 선택</form:option>
                    <c:forEach var="line" items="${lines}">
                        <form:option value="${line}">${line}</form:option>
                    </c:forEach>
                </form:select>
                <form:errors path="subLine" cssClass="field-error"/>
            </div>

            <div class="button-row">
                <a href="/main" class="btn cancel-btn">취소</a>
                <button type="submit" class="btn save-btn">저장하기</button>
            </div>

        </form:form>
    </div>

</div>

</body>
</html>