<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>롤면서살자</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
            background:
                    radial-gradient(circle at top left, rgba(29, 78, 216, 0.45), transparent 34%),
                    radial-gradient(circle at bottom right, rgba(202, 138, 4, 0.28), transparent 30%),
                    linear-gradient(135deg, #050816 0%, #0f172a 50%, #020617 100%);
            color: #f8fafc;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .page-wrap {
            width: 100%;
            padding: 24px;
        }

        .main-card {
            width: 100%;
            max-width: 480px;
            margin: 0 auto;
            padding: 34px;
            border: 1px solid rgba(212, 175, 55, 0.35);
            border-radius: 26px;
            background: rgba(15, 23, 42, 0.9);
            box-shadow:
                    0 24px 80px rgba(0, 0, 0, 0.5),
                    inset 0 1px 0 rgba(255, 255, 255, 0.06);
        }

        .brand {
            text-align: center;
            margin-bottom: 26px;
        }

        .brand-badge {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 62px;
            height: 62px;
            margin-bottom: 14px;
            border-radius: 20px;
            background: linear-gradient(135deg, #1d4ed8, #0f766e);
            border: 1px solid rgba(250, 204, 21, 0.5);
            font-size: 30px;
            box-shadow: 0 12px 34px rgba(37, 99, 235, 0.35);
        }

        .brand h1 {
            margin: 0;
            font-size: 30px;
            color: #facc15;
            letter-spacing: -0.04em;
        }

        .brand p {
            margin: 8px 0 0;
            color: #94a3b8;
            font-size: 14px;
        }

        .profile-box {
            margin-bottom: 22px;
            padding: 16px;
            border-radius: 18px;
            background: rgba(30, 41, 59, 0.72);
            border: 1px solid rgba(148, 163, 184, 0.18);
        }

        .nickname {
            margin: 0 0 8px;
            font-size: 18px;
            font-weight: 900;
            color: #f8fafc;
        }

        .line-info {
            margin: 0;
            font-size: 14px;
            color: #cbd5e1;
        }

        .line-info strong {
            color: #facc15;
        }

        .menu-grid {
            display: grid;
            grid-template-columns: 1fr;
            gap: 12px;
        }

        .menu-btn {
            display: flex;
            align-items: center;
            justify-content: space-between;
            min-height: 58px;
            padding: 0 18px;
            border-radius: 17px;
            background: rgba(2, 6, 23, 0.62);
            border: 1px solid rgba(148, 163, 184, 0.22);
            color: #f8fafc;
            text-decoration: none;
            font-size: 15px;
            font-weight: 800;
            transition: 0.18s;
        }

        .menu-btn:hover {
            transform: translateY(-1px);
            border-color: rgba(250, 204, 21, 0.6);
            background: rgba(30, 41, 59, 0.85);
            box-shadow: 0 12px 28px rgba(0, 0, 0, 0.28);
        }

        .menu-btn.primary {
            background: linear-gradient(135deg, #ca8a04, #facc15);
            color: #111827;
            border: 0;
        }

        .menu-btn .left {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .menu-btn .icon {
            font-size: 20px;
        }

        .logout-form {
            margin-top: 16px;
        }

        .logout-btn {
            width: 100%;
            height: 48px;
            border-radius: 15px;
            border: 1px solid rgba(248, 113, 113, 0.28);
            background: rgba(127, 29, 29, 0.22);
            color: #fecaca;
            font-size: 15px;
            font-weight: 800;
            cursor: pointer;
            transition: 0.18s;
        }

        .logout-btn:hover {
            background: rgba(127, 29, 29, 0.38);
        }

        .notice {
            margin-top: 18px;
            color: #64748b;
            font-size: 12px;
            text-align: center;
            line-height: 1.5;
        }
    </style>
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