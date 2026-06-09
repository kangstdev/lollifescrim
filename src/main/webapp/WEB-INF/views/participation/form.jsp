<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>참여 신청</title>

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

        .form-card {
            width: 100%;
            max-width: 520px;
            margin: 0 auto;
            padding: 34px;
            border: 1px solid rgba(212, 175, 55, 0.35);
            border-radius: 26px;
            background: rgba(15, 23, 42, 0.92);
            box-shadow:
                    0 24px 80px rgba(0, 0, 0, 0.5),
                    inset 0 1px 0 rgba(255, 255, 255, 0.06);
        }

        .header {
            text-align: center;
            margin-bottom: 24px;
        }

        .badge {
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

        .header h1 {
            margin: 0;
            font-size: 28px;
            color: #facc15;
            letter-spacing: -0.04em;
        }

        .header p {
            margin: 8px 0 0;
            color: #94a3b8;
            font-size: 14px;
        }

        .profile-box {
            margin-bottom: 20px;
            padding: 16px;
            border-radius: 18px;
            background: rgba(30, 41, 59, 0.72);
            border: 1px solid rgba(148, 163, 184, 0.18);
        }

        .profile-box p {
            margin: 0;
            font-size: 14px;
            color: #cbd5e1;
            line-height: 1.7;
        }

        .profile-box strong {
            color: #facc15;
        }

        .error-box {
            margin-bottom: 16px;
            padding: 13px 14px;
            border-radius: 14px;
            background: rgba(127, 29, 29, 0.28);
            border: 1px solid rgba(248, 113, 113, 0.35);
            color: #fecaca;
            font-size: 14px;
            font-weight: 700;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-label {
            display: block;
            margin-bottom: 10px;
            color: #e2e8f0;
            font-size: 15px;
            font-weight: 900;
        }

        .line-grid,
        .time-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 10px;
        }

        .option-card {
            position: relative;
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 52px;
            border-radius: 16px;
            background: rgba(2, 6, 23, 0.62);
            border: 1px solid rgba(148, 163, 184, 0.22);
            color: #f8fafc;
            font-size: 15px;
            font-weight: 800;
            cursor: pointer;
            transition: 0.18s;
        }

        .option-card:hover {
            transform: translateY(-1px);
            border-color: rgba(250, 204, 21, 0.55);
            background: rgba(30, 41, 59, 0.85);
        }

        .option-card input {
            position: absolute;
            opacity: 0;
            pointer-events: none;
        }

        .option-card:has(input:checked) {
            background: linear-gradient(135deg, #ca8a04, #facc15);
            color: #111827;
            border-color: transparent;
        }

        .memo {
            width: 100%;
            min-height: 90px;
            padding: 14px;
            border-radius: 16px;
            border: 1px solid rgba(148, 163, 184, 0.22);
            background: rgba(2, 6, 23, 0.62);
            color: #f8fafc;
            font-size: 14px;
            resize: vertical;
            outline: none;
        }

        .memo:focus {
            border-color: rgba(250, 204, 21, 0.65);
        }

        .button-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 10px;
            margin-top: 22px;
        }

        .submit-btn,
        .back-btn {
            height: 50px;
            border-radius: 16px;
            border: 0;
            font-size: 15px;
            font-weight: 900;
            cursor: pointer;
            text-decoration: none;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .submit-btn {
            background: linear-gradient(135deg, #ca8a04, #facc15);
            color: #111827;
        }

        .back-btn {
            background: rgba(2, 6, 23, 0.62);
            border: 1px solid rgba(148, 163, 184, 0.22);
            color: #f8fafc;
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
    <div class="form-card">

        <div class="header">
            <div class="badge">
                <c:choose>
                    <c:when test="${participationType.name() == 'NORMAL'}">🔥</c:when>
                    <c:when test="${participationType.name() == 'FREE'}">🌀</c:when>
                    <c:when test="${participationType.name() == 'FLEX'}">🏆</c:when>
                    <c:when test="${participationType.name() == 'ARAM'}">❄️</c:when>
                    <c:otherwise>⚔️</c:otherwise>
                </c:choose>
            </div>

            <h1>${participationType.label} 참여 신청</h1>
            <p>참여 라인과 가능한 시간을 선택해주세요.</p>
        </div>

        <div class="profile-box">
            <p>
                닉네임 <strong>${loginMember.nickname}</strong>
            </p>
            <p>
                주라인 <strong>${loginMember.mainLine}</strong>
                · 부라인 <strong>${loginMember.subLine}</strong>
            </p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="error-box">
                    ${errorMessage}
            </div>
        </c:if>

        <form action="/participation" method="post">
            <input type="hidden" name="participationType" value="${participationType}">

            <div class="form-group">
                <label class="form-label">참여 라인</label>

                <div class="line-grid">
                    <c:forEach var="line" items="${lines}">
                        <label class="option-card">
                            <input type="radio" name="selectedLine" value="${line}" required>

                            <span>
                                <c:choose>
                                    <c:when test="${line.name() == 'TOP'}">탑</c:when>
                                    <c:when test="${line.name() == 'JUNGLE'}">정글</c:when>
                                    <c:when test="${line.name() == 'MID'}">미드</c:when>
                                    <c:when test="${line.name() == 'AD'}">원딜</c:when>
                                    <c:when test="${line.name() == 'SUPPORT'}">서폿</c:when>
                                    <c:otherwise>${line}</c:otherwise>
                                </c:choose>
                            </span>
                        </label>
                    </c:forEach>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">참여 가능 시간</label>

                <div class="time-grid">
                    <c:forEach var="time" items="${times}">
                        <label class="option-card">
                            <input type="radio" name="availableTime" value="${time}" required>
                            <span>${time}시</span>
                        </label>
                    </c:forEach>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">메모</label>
                <textarea class="memo" name="memo" placeholder="전달할 내용이 있으면 적어주세요."></textarea>
            </div>

            <div class="button-row">
                <a href="/main" class="back-btn">메인으로</a>
                <button type="submit" class="submit-btn">참여 신청</button>
            </div>
        </form>

        <div class="notice">
            일반 내전은 가입 시 설정한 주라인/부라인만 선택 가능합니다.
        </div>

    </div>
</div>

</body>
</html>