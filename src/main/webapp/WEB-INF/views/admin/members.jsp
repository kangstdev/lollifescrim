<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원 관리</title>
    <link rel="stylesheet" href="/css/notice/notice.css">
    <link rel="stylesheet" href="<c:url value='/css/common/mobile.css' />">
</head>
<body>
<div class="notice-page">
    <div class="notice-container">

        <div class="notice-header">
            <div>
                <p class="notice-subtitle">관리자</p>
                <h1 class="notice-title">회원 관리</h1>
                <p class="notice-desc">
                    회원 목록을 확인하고 비밀번호를 0000으로 초기화할 수 있습니다.
                </p>
            </div>

            <div class="notice-actions">
                <a class="notice-btn secondary" href="/admin">관리자 홈</a>
                <a class="notice-btn secondary" href="/main">메인으로</a>
            </div>
        </div>

        <div class="notice-list-box">
            <c:choose>
                <c:when test="${empty members}">
                    <div class="notice-empty">
                        <p class="notice-empty-title">가입된 회원이 없습니다.</p>
                    </div>
                </c:when>

                <c:otherwise>
                    <c:forEach var="member" items="${members}">
                        <div class="admin-row">
                            <div class="admin-row-main">
                                <div class="admin-row-title">
                                    <c:out value="${member.nickname}" />
                                    <span class="notice-badge">${member.role}</span>
                                </div>

                                <div class="admin-row-desc">
                                    주라인 ${member.mainLine}
                                    · 부라인 ${member.subLine}
                                    · 티어 ${member.tier}
                                </div>
                            </div>

                        <div class="admin-row-actions">
                            <form method="post"
                                  action="/admin/members/${member.id}/reset-password"
                                  onsubmit="return confirm('비밀번호를 0000으로 초기화하시겠습니까?');">
                                <button type="submit" class="notice-btn danger">
                                    비밀번호 초기화
                                </button>
                            </form>


                            <c:if test="${member.id ne loginMember.id}">
                                <form method="post"
                                      action="/admin/members/${member.id}/deactivate"
                                      onsubmit="return confirm('해당 회원을 삭제 처리하시겠습니까?\\n참여 신청은 삭제되고, 계정은 로그인 불가 처리됩니다.');">
                                    <button type="submit" class="notice-btn danger">
                                        회원비활성화
                                    </button>
                                </form>
                            </c:if>
                        </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

    </div>
</div>
</body>
</html>