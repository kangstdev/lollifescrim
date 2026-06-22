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
            <br />
            <a href="https://naejeon.lol/"
               target="_blank"
               rel="noopener noreferrer"
               style="display:inline-block; padding:10px 14px; background:#2563eb; color:white; text-decoration:none; border-radius:8px; font-weight:600;">
                내전통계 사이트이동
            </a>
            <br />
            <br />
            <a href="https://www.youtube.com/results?search_query=%EB%A1%A4%EB%A9%B4%EC%84%9C%EC%82%B4%EC%9E%90"
               target="_blank"
               rel="noopener noreferrer"
               style="display:inline-block; padding:10px 14px; background:#ef4444; color:white; text-decoration:none; border-radius:8px; font-weight:600;">
                롤면서 살자 유튜브
            </a>
        </div>

<div class="profile-box">
    <a href="/member/edit" class="profile-edit-btn" title="개인정보 수정">
        수정
    </a>

    <p class="nickname">${loginMember.nickname}님</p>
    <p class="line-info">
        주라인 <strong>${loginMember.mainLine}</strong>
        · 부라인 <strong>${loginMember.subLine}</strong>
        · 티어 <strong>${loginMember.tier}</strong>
    </p>
</div>

        <div class="menu-grid">

            <a class="menu-card" href="/participation/status">
                <span class="menu-icon">📋</span>
                <span class="menu-title">내 참여 현황</span>
            </a>

             <a class="menu-card create" href="/party/form">
                 <span class="menu-icon">➕</span>
                  <span class="menu-title">파티 만들기</span>
             </a>


            <a class="menu-card primary" href="/participation/form?type=NORMAL">
                <span class="menu-icon">🔥</span>
                <span class="menu-title">내전 참여</span>
            </a>


            <a class="menu-card" href="/participation/form?type=FREE">
                <span class="menu-icon">🌀</span>
                <span class="menu-title">자유내전</span>
            </a>

            <a class="menu-card" href="/participation/form?type=FLEX">
                <span class="menu-icon">🏆</span>
                <span class="menu-title">자유랭크</span>
            </a>

            <a class="menu-card" href="/participation/form?type=ARAM">
                <span class="menu-icon">❄️</span>
                <span class="menu-title">증바람</span>
            </a>

            <a class="menu-card" href="/participation/list?type=NORMAL">
                <span class="menu-icon">👥</span>
                <span class="menu-title">참여 현황</span>
            </a>

            <a class="menu-card" href="/notice/list">
                <span class="menu-icon">📝</span>
                <span class="menu-title">공지/건의</span>
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