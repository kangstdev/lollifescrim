<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>롤면서살자 | 회원가입</title>

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

        .join-card {
            width: 100%;
            max-width: 520px;
            margin: 0 auto;
            padding: 34px;
            border: 1px solid rgba(212, 175, 55, 0.35);
            border-radius: 24px;
            background: rgba(15, 23, 42, 0.9);
            box-shadow:
                    0 24px 80px rgba(0, 0, 0, 0.5),
                    inset 0 1px 0 rgba(255, 255, 255, 0.06);
            backdrop-filter: blur(16px);
        }

        .brand {
            text-align: center;
            margin-bottom: 28px;
        }

        .brand-badge {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 58px;
            height: 58px;
            margin-bottom: 14px;
            border-radius: 18px;
            background: linear-gradient(135deg, #1d4ed8, #0f766e);
            border: 1px solid rgba(250, 204, 21, 0.45);
            box-shadow: 0 10px 30px rgba(37, 99, 235, 0.35);
            font-size: 28px;
        }

        .brand h1 {
            margin: 0;
            font-size: 28px;
            letter-spacing: -0.04em;
            color: #facc15;
        }

        .brand p {
            margin: 8px 0 0;
            font-size: 14px;
            color: #94a3b8;
        }

        .error-message {
            margin-bottom: 18px;
            padding: 12px 14px;
            border-radius: 12px;
            background: rgba(239, 68, 68, 0.12);
            border: 1px solid rgba(248, 113, 113, 0.35);
            color: #fecaca;
            font-size: 14px;
        }

        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 16px;
        }

        .form-group {
            margin-bottom: 18px;
        }

        .form-group.full {
            grid-column: 1 / -1;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-size: 14px;
            font-weight: 700;
            color: #cbd5e1;
        }

        input,
        select {
            width: 100%;
            height: 48px;
            padding: 0 14px;
            border-radius: 14px;
            border: 1px solid rgba(148, 163, 184, 0.28);
            background: rgba(2, 6, 23, 0.65);
            color: #f8fafc;
            font-size: 15px;
            outline: none;
            transition: 0.18s;
        }

        input::placeholder {
            color: #64748b;
        }

        select {
            cursor: pointer;
        }

        option {
            background: #0f172a;
            color: #f8fafc;
        }

        input:focus,
        select:focus {
            border-color: #facc15;
            box-shadow: 0 0 0 4px rgba(250, 204, 21, 0.12);
        }

        .line-help {
            margin-top: -6px;
            margin-bottom: 18px;
            padding: 12px 14px;
            border-radius: 14px;
            background: rgba(30, 41, 59, 0.65);
            color: #94a3b8;
            font-size: 13px;
            line-height: 1.5;
        }

        .line-help strong {
            color: #eab308;
        }

        .submit-btn {
            width: 100%;
            height: 50px;
            margin-top: 4px;
            border: 0;
            border-radius: 15px;
            background: linear-gradient(135deg, #ca8a04, #facc15);
            color: #111827;
            font-size: 16px;
            font-weight: 900;
            cursor: pointer;
            transition: 0.18s;
        }

        .submit-btn:hover {
            transform: translateY(-1px);
            box-shadow: 0 14px 28px rgba(250, 204, 21, 0.22);
        }

        .bottom-link {
            margin-top: 22px;
            text-align: center;
            font-size: 14px;
            color: #94a3b8;
        }

        .bottom-link a {
            color: #60a5fa;
            font-weight: 800;
            text-decoration: none;
        }

        .bottom-link a:hover {
            text-decoration: underline;
        }

        @media (max-width: 540px) {
            .join-card {
                padding: 28px 22px;
            }

            .form-grid {
                grid-template-columns: 1fr;
                gap: 0;
            }
        }
    </style>
</head>
<body>

<div class="page-wrap">
    <div class="join-card">

        <div class="brand">
            <div class="brand-badge">🛡️</div>
            <h1>롤면서살자</h1>
            <p>내전 참여를 위한 모임원 등록</p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="error-message">
                    ${errorMessage}
            </div>
        </c:if>

        <form action="/join" method="post">

            <div class="form-group">
                <label>닉네임</label>
                <input type="text" name="nickname" placeholder="모임에서 사용하는 닉네임">
            </div>

            <div class="form-group">
                <label>비밀번호</label>
                <input type="password" name="password" placeholder="비밀번호 입력">
            </div>

            <div class="form-group">
                <label>공식 인증번호</label>
                <input type="text" name="inviteCode" placeholder="운영진에게 받은 인증번호">
            </div>

            <div class="line-help">
                <strong>일반 내전 안내</strong><br>
                일반 내전에서는 가입 시 선택한 주라인/부라인만 투표할 수 있습니다.
            </div>

            <div class="form-grid">
                <div class="form-group">
                    <label>주라인</label>
                    <select name="mainLine">
                        <option value="">선택</option>
                        <c:forEach var="line" items="${lines}">
                            <option value="${line}">${line}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label>부라인</label>
                    <select name="subLine">
                        <option value="">선택</option>
                        <c:forEach var="line" items="${lines}">
                            <option value="${line}">${line}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <button type="submit" class="submit-btn">가입하기</button>
        </form>

        <div class="bottom-link">
            이미 계정이 있나요?
            <a href="/login">로그인</a>
        </div>

    </div>
</div>

</body>
</html>