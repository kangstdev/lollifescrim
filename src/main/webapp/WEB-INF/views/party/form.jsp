<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>파티 만들기</title>

    <link rel="stylesheet" href="<c:url value='/css/party/form.css' />">
</head>
<body>

<div class="page-wrap">
    <div class="form-card">

        <div class="header">
            <div class="badge">➕</div>
            <h1>파티 만들기</h1>
            <p>원하는 시간과 종류를 선택해서 새 파티를 만들어보세요.</p>
        </div>

        <div class="profile-box">
            <p>
                만든 사람 <strong>${loginMember.nickname}</strong>
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

        <form action="/party" method="post">

            <div class="form-group">
                <label class="form-label">파티 종류</label>

                <div class="type-grid">
                    <c:forEach var="type" items="${participationTypes}">
                        <label class="option-card">
                            <input
                                    type="radio"
                                    name="participationType"
                                    value="${type.name()}"
                                    required
                                    <c:if test="${selectedParticipationType == type}">checked</c:if>
                            >

                            <span>
                                <c:choose>
                                    <c:when test="${type.name() == 'NORMAL'}">🔥 내전</c:when>
                                    <c:when test="${type.name() == 'FREE'}">🌀 자유내전</c:when>
                                    <c:when test="${type.name() == 'FLEX'}">🏆 자유랭크</c:when>
                                    <c:when test="${type.name() == 'ARAM'}">❄️ 증바람</c:when>
                                    <c:otherwise>${type}</c:otherwise>
                                </c:choose>
                            </span>
                        </label>
                    </c:forEach>
                </div>
            </div>

            <div class="form-group" id="creatorLineGroup">
                <label class="form-label">내 라인 선택</label>

                <div class="line-grid">
                    <c:forEach var="line" items="${lines}">
                        <label class="option-card line-option">
                            <input
                                    type="radio"
                                    name="creatorLine"
                                    value="${line.name()}"
                                    <c:if test="${creatorLine == line}">checked</c:if>
                            >
                            <span>${line}</span>
                        </label>
                    </c:forEach>
                </div>

                <p class="help-text">
                    내전, 자유내전, 자유랭크는 파티장이 본인 라인을 선택해야 합니다.
                </p>
            </div>

            <div class="form-group">
                <label class="form-label">파티 시간</label>

                <input
                        type="number"
                        name="partyTime"
                        class="text-input"
                        min="0"
                        max="23"
                        placeholder="예: 19"
                        value="${partyTime}"
                        required
                >

                <p class="help-text">
                    숫자만 입력해주세요. 예: 19 → 19시
                </p>
            </div>

            <div class="form-group">
                <label class="form-label">파티 제목</label>

                <input
                        type="text"
                        name="title"
                        class="text-input"
                        maxlength="100"
                        placeholder="예: 19시 자유내전 구합니다"
                        value="${title}"
                        required
                >
            </div>

            <div class="form-group">
                <label class="form-label">메모</label>

                <textarea
                        name="memo"
                        class="memo"
                        placeholder="전달할 내용이 있으면 적어주세요. 예: 즐겜 위주, 마이크 가능"
                >${memo}</textarea>
            </div>

            <div class="button-row">
                <a href="/main" class="back-btn">메인으로</a>
                <button type="submit" class="submit-btn">파티 만들기</button>
            </div>
        </form>

        <div class="notice">
            만든 파티는 해당 항목별 참여 현황에서 확인할 수 있습니다.
        </div>

    </div>
</div>

<script>
    const typeInputs = document.querySelectorAll("input[name='participationType']");
    const creatorLineGroup = document.getElementById("creatorLineGroup");
    const creatorLineInputs = document.querySelectorAll("input[name='creatorLine']");

    function updateCreatorLineVisibility() {
        const checkedType = document.querySelector("input[name='participationType']:checked");

        if (!checkedType) {
            creatorLineGroup.style.display = "none";
            creatorLineInputs.forEach(input => {
                input.required = false;
            });
            return;
        }

        if (checkedType.value === "ARAM") {
            creatorLineGroup.style.display = "none";
            creatorLineInputs.forEach(input => {
                input.checked = false;
                input.required = false;
            });
        } else {
            creatorLineGroup.style.display = "block";
            creatorLineInputs.forEach(input => {
                input.required = true;
            });
        }
    }

    typeInputs.forEach(input => {
        input.addEventListener("change", updateCreatorLineVisibility);
    });

    updateCreatorLineVisibility();
</script>

</body>
</html>