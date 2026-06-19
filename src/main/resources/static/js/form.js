/*
 * 참여 신청 화면 전용 JS
 *
 * 담당 기능:
 * 1. 이미 신청된 시간 마감 처리
 * 2. 신청 시 시간 최소 1개 선택 검사
 * 3. 시간 선택 시 현재 선택 시간 현황 영역 표시
 */

/*
 * 라인 radio 버튼 목록
 */
const lineRadios = document.querySelectorAll("input[name='selectedLine']");

/*
 * 현재 선택한 시간 현황 박스
 */
const currentStatusBox = document.getElementById("currentStatusBox");

/*
 * 시간 버튼은 고정 시간 + 사용자 설정 시간이 같이 있을 수 있습니다.
 * 그래서 매번 전체 .time-option을 다시 찾습니다.
 */
function getTimeOptions() {
    return document.querySelectorAll(".time-option");
}

/*
 * 시간 버튼 초기화
 *
 * 라인을 바꿀 때마다 기존 마감 표시를 지우고
 * 다시 계산하기 위해 사용합니다.
 */
function resetTimeOptions() {
    const timeOptions = getTimeOptions();

    timeOptions.forEach(function (label) {
        const checkbox = label.querySelector(".time-checkbox");

        if (!checkbox) {
            return;
        }

        checkbox.disabled = false;

        label.classList.remove("disabled");
        label.classList.remove("occupied");

        const closedText = label.querySelector(".closed-text");

        if (closedText) {
            closedText.remove();
        }
    });
}

/*
 * 선택한 라인 기준으로 이미 신청된 시간 disabled 처리
 *
 * 예:
 * 선택 라인 = MID
 * 시간 = 21
 * slotKey = 21_MID
 *
 * occupiedSlotKeys 안에 21_MID가 있으면 해당 시간은 마감 처리됩니다.
 */
function applyOccupiedTimes(selectedLine) {
    resetTimeOptions();

    if (!selectedLine) {
        return;
    }

    const timeOptions = getTimeOptions();

    timeOptions.forEach(function (label) {
        const checkbox = label.querySelector(".time-checkbox");
        const timeLabel = label.querySelector(".time-label");

        if (!checkbox || !timeLabel) {
            return;
        }

        const time = checkbox.dataset.time;
        const slotKey = time + "_" + selectedLine;

        if (occupiedSlotKeys.includes(slotKey)) {
            checkbox.checked = false;
            checkbox.disabled = true;

            label.classList.add("disabled");
            label.classList.add("occupied");

            const closedText = document.createElement("span");
            closedText.className = "closed-text";
            closedText.textContent = "마감";

            timeLabel.appendChild(closedText);
        }
    });
}

/*
 * 현재 선택된 라인 가져오기
 */
function getSelectedLine() {
    const checkedLine = document.querySelector("input[name='selectedLine']:checked");

    if (!checkedLine) {
        return null;
    }

    return checkedLine.dataset.lineName;
}

/*
 * 현재 체크된 시간 목록 가져오기
 */
function getCheckedTimes() {
    return Array.from(
        document.querySelectorAll("input[name='selectedTimes']:checked")
    ).map(function (checkbox) {
        return checkbox.value;
    });
}

/*
 * 시간 선택 시 아래 현황 박스 갱신
 *
 * 지금은 실제 참여자 목록 조회 전 단계라서,
 * 선택한 시간만 표시합니다.
 *
 * 다음 단계에서 이 부분에
 * 라인별 참여자 / 증바람 참여자 목록을 넣으면 됩니다.
 */
function updateCurrentStatusBox() {
    if (!currentStatusBox) {
        return;
    }

    const checkedTimes = getCheckedTimes();

    if (checkedTimes.length === 0) {
        currentStatusBox.innerHTML =
            "<p class='status-empty-text'>시간을 선택하면 해당 시간의 참여 현황이 표시됩니다.</p>";
        return;
    }

    const selectedTimeText = checkedTimes
        .map(function (time) {
            return time + "시";
        })
        .join(", ");

    if (participationType === "ARAM") {
        currentStatusBox.innerHTML =
            "<p class='status-title'>" + selectedTimeText + " 증바람 참여자</p>" +
            "<p class='status-empty-text'>다음 단계에서 이 시간대 참여자 닉네임이 표시됩니다.</p>";
        return;
    }

    currentStatusBox.innerHTML =
        "<p class='status-title'>" + selectedTimeText + " 참여 현황</p>" +
        "<p class='status-empty-text'>다음 단계에서 이 시간대 라인별 참여 현황이 표시됩니다.</p>";
}

/*
 * 라인을 선택할 때마다 해당 라인의 마감 시간을 다시 계산
 */
lineRadios.forEach(function (radio) {
    radio.addEventListener("change", function () {
        const selectedLine = this.dataset.lineName;
        applyOccupiedTimes(selectedLine);
        updateCurrentStatusBox();
    });
});

/*
 * 시간 버튼을 누를 때마다 현재 선택한 시간 현황 박스 갱신
 */
getTimeOptions().forEach(function (label) {
    const checkbox = label.querySelector(".time-checkbox");

    if (!checkbox) {
        return;
    }

    checkbox.addEventListener("change", function () {
        updateCurrentStatusBox();
    });
});

/*
 * 신청 버튼 클릭 시 시간 최소 1개 선택 검사
 */
document.getElementById("participationForm").addEventListener("submit", function (event) {
    const checkedTimes = document.querySelectorAll("input[name='selectedTimes']:checked");

    if (checkedTimes.length === 0) {
        event.preventDefault();
        alert("참여 가능한 시간을 최소 1개 선택해주세요.");
    }
});

/*
 * 페이지 진입 시 이미 선택된 라인이 있으면 마감 시간 적용
 * 지금은 보통 선택된 라인이 없지만, 나중에 수정 기능 만들 때 필요합니다.
 */
if (participationType !== "ARAM") {
    const selectedLine = getSelectedLine();

    if (selectedLine) {
        applyOccupiedTimes(selectedLine);
    }
}

/*
 * 페이지 진입 시 현황 박스 초기화
 */
updateCurrentStatusBox();