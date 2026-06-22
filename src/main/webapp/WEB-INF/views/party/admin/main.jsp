<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>관리자</title>
    <link rel="stylesheet" href="/css/notice/notice.css">
    <link rel="stylesheet" href="<c:url value='/css/common/mobile.css' />">
</head>
<body>
<div class="notice-page">
    <div class="notice-container">

        <div class="notice-header">
            <div>
                <p class="notice-subtitle">관리자</p>
                <h1 class="notice-title">관리자 페이지</h1>
                <p class="notice-desc">
                    회원 관리, 참여 관리, 공지사항과 건의사항을 관리할 수 있습니다.
                </p>
            </div>

            <div class="notice-actions">
                <a class="notice-btn secondary" href="/main">메인으로</a>
            </div>
        </div>

        <div class="notice-list-box">

            <a class="notice-item" href="/admin/members">
                <div class="notice-item-main">
                    <span class="notice-badge">회원</span>
                    <span class="notice-item-title">회원 관리</span>
                </div>
                <div class="notice-item-meta">
                    <span>회원 목록 / 비밀번호 초기화 / 삭제</span>
                </div>
            </a>

            <a class="notice-item" href="/admin/participations">
                <div class="notice-item-main">
                    <span class="notice-badge">참여</span>
                    <span class="notice-item-title">참여 관리</span>
                </div>
                <div class="notice-item-meta">
                    <span>참여 현황 조회 / 강제 취소</span>
                </div>
            </a>

            <a class="notice-item" href="/notice/list">
                <div class="notice-item-main">
                    <span class="notice-badge">공지</span>
                    <span class="notice-item-title">공지사항 관리</span>
                </div>
                <div class="notice-item-meta">
                    <span>공지 작성 / 수정 / 삭제</span>
                </div>
            </a>

            <a class="notice-item" href="/suggestion/list">
                <div class="notice-item-main">
                    <span class="notice-badge">건의</span>
                    <span class="notice-item-title">건의사항 관리</span>
                </div>
                <div class="notice-item-meta">
                    <span>건의 확인 / 상태 변경</span>
                </div>
            </a>

        </div>

    </div>
</div>
</body>
</html>