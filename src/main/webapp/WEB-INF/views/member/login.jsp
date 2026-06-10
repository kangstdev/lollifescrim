<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>롤면서살자 | 로그인</title>
        <link rel="stylesheet" href="<c:url value='/css/member/login.css' />">
</head>
<body>

<div class="page-wrap">
    <div class="login-card">

        <div class="brand">
            <div class="brand-badge">⚔️</div>
            <h1>롤면서살자</h1>
            <p>내전 참가를 위한 로그인</p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="error-message">
                    ${errorMessage}
            </div>
        </c:if>

        <form action="/login" method="post">
            <div class="form-group">
                <label>닉네임</label>
                <input type="text" name="nickname" placeholder="모임에서 사용하는 닉네임">
            </div>

            <div class="form-group">
                <label>비밀번호</label>
                <input type="password" name="password" placeholder="비밀번호 입력">
            </div>

            <button type="submit" class="submit-btn">로그인</button>
        </form>

        <div class="bottom-link">
            아직 가입하지 않았나요?
            <a href="/join">회원가입</a>
        </div>

        <div class="notice">
            <strong>안내</strong><br>
            롤면서살자 모임원 전용 내전 페이지입니다.
        </div>

    </div>
</div>

</body>
</html>