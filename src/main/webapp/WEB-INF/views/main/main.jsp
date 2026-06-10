<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>


<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>롤면서살자</title>

    <link rel="stylesheet" href="<c:url value='/css/main/main.css' />">
</head>
<body>

<div class="page-wrap">
    <div class="main-card">

        <div class="brand">
            <div class="brand-badge">⚔️</div>
            <h1>롤면서살자</h1>
            <p>내전 · 자유랭크 · 증바람 참여 신청</p>
        </div>

        <div class="profile-box">
            <p class="nickname">${loginMember.nickname}님</p>
            <p class="line-info">
                주라인 <strong>${loginMember.mainLine}</strong>
                · 부라인 <strong>${loginMember.subLine}</strong>
            </p>
        </div>

        <div class="menu-grid">
            <a class="menu-btn" href="/participation/status">
                <span class="left">
                    <span class="icon">📋</span>
                    <span>내 참여 현황</span>
                </span>
                <span>›</span>
            </a>

            <a class="menu-btn primary" href="/participation/form?type=NORMAL">
                <span class="left">
                    <span class="icon">🔥</span>
                    <span>내전 참여</span>
                </span>
                <span>›</span>
            </a>

            <a class="menu-btn" href="/participation/form?type=FREE">
                <span class="left">
                    <span class="icon">🌀</span>
                    <span>자유내전 참여</span>
                </span>
                <span>›</span>
            </a>

            <a class="menu-btn" href="/participation/form?type=FLEX">
                <span class="left">
                    <span class="icon">🏆</span>
                    <span>자유랭크 파티</span>
                </span>
                <span>›</span>
            </a>

            <a class="menu-btn" href="/participation/form?type=ARAM">
                <span class="left">
                    <span class="icon">❄️</span>
                    <span>증바람 참여</span>
                </span>
                <span>›</span>
            </a>
        </div>

        <form class="logout-form" action="/logout" method="post">
            <button class="logout-btn" type="submit">로그아웃</button>
        </form>

        <div class="notice">
            일반 내전은 가입 시 설정한 주라인/부라인만 선택 가능합니다.
        </div>

    </div>
</div>

</body>
</html>