<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>내전 목록</title>
</head>
<body>

<h1>내전 목록</h1>

<p>${loginMember.nickname}님 로그인 중</p>
<p>주라인: ${loginMember.mainLine}</p>
<p>부라인: ${loginMember.subLine}</p>

<form action="/logout" method="post">
    <button type="submit">로그아웃</button>
</form>

</body>
</html>