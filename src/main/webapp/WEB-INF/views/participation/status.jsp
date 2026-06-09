<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>롤면서살자 | 참여 현황</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
            background:
                    radial-gradient(circle at top, rgba(29, 78, 216, 0.42), transparent 34%),
                    linear-gradient(135deg, #050816 0%, #0f172a 48%, #020617 100%);
            color: #f8fafc;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .page-wrap {
            width: 100%;
            padding: 24px;
        }

        .status-card {
            width: 100%;
            max-width: 620px;
            margin: 0 auto;
            padding: 34px;
            border-radius: 26px;
            border: 1px solid rgba(212, 175, 55, 0.35);
            background: rgba(15, 23, 42, 0.92);
            box-shadow: 0 24px 80px rgba(0, 0, 0, 0.5);
        }

        .top-nav {
            margin-bottom: 18px;
        }

        .back-link {
            color: #93c5fd;
            font-size: 14px;
            font-weight: 800;
            text-decoration: none;
        }

        .back-link:hover {
            text-decoration: underline;
        }

        h1 {
            margin: 0 0 8px;
            font-size: 28px;
            color: #facc15;
            letter-spacing: -0.04em;
        }

        .subtitle {
            margin: 0 0 24px;
            color: #94a3b8;
            font-size: 14px;
        }

        .empty {
            padding: 26px 18px;
            border-radius: 18px;
            background: rgba(30, 41, 59, 0.7);
            text-align: center;
            color: #94a3b8;
            font-size: 14px;
        }

        .list {
            display: grid;
            gap: 12px;
        }

        .item {
            padding: 18px;
            border-radius: 18px;
            background: rgba(2, 6, 23, 0.62);
            border: 1px solid rgba(148, 163, 184, 0.22);
        }

        .item-head {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 12px;
            margin-bottom: 12px;
        }

        .type {
            font-size: 17px;
            font-weight: 900;
            color: #f8fafc;
        }

        .badge {
            padding: 6px 10px;
            border-radius: 999px;
            background: rgba(250, 204, 21, 0.14);
            border: 1px solid rgba(250, 204, 21, 0.35);
            color: #fde68a;
            font-size: 12px;
            font-weight: 900;
        }

        .meta {
            margin: 6px 0 0;
            color: #cbd5e1;
            font-size: 14px;
            line-height: 1.6;
        }

        .meta strong {
            color: #93c5fd;
        }

        .quick-links {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 10px;
            margin-top: 24px;
        }

        .quick-links a {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 46px;
            border-radius: 14px;
            text-decoration: none;
            color: #f8fafc;
            background: rgba(30, 41, 59, 0.75);
            border: 1px solid rgba(148, 163, 184, 0.22);
            font-weight: 800;
            font-size: 14px;
        }

        .quick-links a.primary {
            background: linear-gradient(135deg, #ca8a04, #facc15);
            color: #111827;
            border: 0;
        }

        @media (max-width: 560px) {
            .status-card {
                padding: 28px 22px;
            }

            .quick-links {
                grid-template-columns: 1fr;
            }

            .item-head {
                align-items: flex-start;
                flex-direction: column;
            }
        }
    </style>
</head>
<body>

<div class="page-wrap">
    <div class="status-card">

        <div class="top-nav">
            <a class="back-link" href="/main">← 메인으로</a>
        </div>

        <h1>내 참여 현황</h1>
        <p class="subtitle">${loginMember.nickname}님의 현재 참여 신청 목록입니다.</p>

        <c:choose>
            <c:when test="${empty participations}">
                <div class="empty">
                    아직 참여 신청 내역이 없습니다.
                </div>
            </c:when>

            <c:otherwise>
                <div class="list">
                    <c:forEach var="p" items="${participations}">
                        <div class="item">
                            <div class="item-head">
                                <div class="type">${p.participationType.label}</div>
                                <div class="badge">신청 완료</div>
                            </div>

                            <p class="meta">
                                <strong>선택 라인</strong> :
                                <c:choose>
                                    <c:when test="${p.selectedLine == 'TOP'}">탑</c:when>
                                    <c:when test="${p.selectedLine == 'JUNGLE'}">정글</c:when>
                                    <c:when test="${p.selectedLine == 'MID'}">미드</c:when>
                                    <c:when test="${p.selectedLine == 'ADC'}">원딜</c:when>
                                    <c:when test="${p.selectedLine == 'SUPPORT'}">서폿</c:when>
                                    <c:otherwise>${p.selectedLine}</c:otherwise>
                                </c:choose>
                            </p>

                            <p class="meta">
                                <strong>참여 가능 시간</strong> :
                                    ${fn:replace(fn:replace(fn:replace(p.selectedTimes, '20', '오후 8시'), '21', '오후 9시'), '22', '오후 10시')}
                            </p>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="quick-links">
            <a class="primary" href="/participation/form?type=NORMAL">내전 참여</a>
            <a href="/participation/form?type=FLEX">자유랭크 참여</a>
        </div>

    </div>
</div>

</body>
</html>