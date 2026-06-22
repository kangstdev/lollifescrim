<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>내전 목록</title>
    <link rel="stylesheet" href="<c:url value='/css/common/mobile.css' />">
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