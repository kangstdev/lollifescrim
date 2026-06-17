/*
 * 참여 신청 화면 전용 JS
 *
 * 담당 기능:
 * 1. 이미 신청된 시간 마감 처리
 * 2. 사용자가 원하는 시간 직접 추가
 * 3. 신청 시 시간 최소 1개 선택 검사
 */

/*
 * 라인 radio 버튼 목록
 */
const lineRadios = document.querySelectorAll("input[name='selectedLine']");

/*
 * 시간 버튼이 들어가는 영역
 */
const timeGrid = document.getElementById("timeGrid");

/*
 * 직접 시간 추가 input / button
 */
const customTimeInput = document.getElementById("customTimeInput");
const addTimeBtn = document.getElementById("addTimeBtn");

/*
 * 시간 버튼은 사용자가 직접 추가할 수 있습니다.
 * 그래서 처음에 한 번만 가져오면 새로 추가된 시간은 인식하지 못합니다.
 * 필요할 때마다 다시 찾도록 함수로 만들었습니다.
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
 * 사용자가 직접 시간 추가
 *
 * 예:
 * 19 입력 후 시간 추가 클릭
 * → 19시 버튼 생성
 * → 자동 체크
 * → 신청 시 selectedTimes=19 로 서버에 전달
 */
function addCustomTime() {
    const time = customTimeInput.value.trim();

    if (time === "") {
        alert("추가할 시간을 입력해주세요.");
        return;
    }

    const timeNumber = Number(time);

    if (Number.isNaN(timeNumber) || timeNumber < 0 || timeNumber > 23) {
        alert("시간은 0부터 23 사이 숫자로 입력해주세요.");
        return;
    }

    /*
     * 09처럼 입력해도 9로 저장합니다.
     */
    const normalizedTime = String(timeNumber);

    /*
     * 이미 같은 시간이 있으면 새로 만들지 않고 기존 시간 체크
     */
    const alreadyExists = document.querySelector(
        "input[name='selectedTimes'][value='" + normalizedTime + "']"
    );

    if (alreadyExists) {
        alert(normalizedTime + "시는 이미 있습니다.");

        if (!alreadyExists.disabled) {
            alreadyExists.checked = true;
        }

        customTimeInput.value = "";
        return;
    }

    /*
     * 새 시간 버튼 생성
     */
    const label = document.createElement("label");
    label.className = "option-card time-option";
    label.dataset.time = normalizedTime;

    label.innerHTML =
        "<input type='checkbox' " +
        "name='selectedTimes' " +
        "value='" + normalizedTime + "' " +
        "class='time-checkbox' " +
        "data-time='" + normalizedTime + "' " +
        "checked>" +
        "<span class='time-label'>" + normalizedTime + "시</span>";

    timeGrid.appendChild(label);

    /*
     * 새로 추가한 시간도 현재 선택한 라인 기준으로
     * 마감 여부를 다시 계산합니다.
     */
    const selectedLine = getSelectedLine();

    if (selectedLine) {
        applyOccupiedTimes(selectedLine);
    }

    customTimeInput.value = "";
}

/*
 * 라인을 선택할 때마다 해당 라인의 마감 시간을 다시 계산
 */
lineRadios.forEach(function (radio) {
    radio.addEventListener("change", function () {
        const selectedLine = this.dataset.lineName;
        applyOccupiedTimes(selectedLine);
    });
});

/*
 * 시간 추가 버튼 클릭
 */
if (addTimeBtn) {
    addTimeBtn.addEventListener("click", addCustomTime);
}

/*
 * 시간 입력 후 Enter를 눌러도 추가
 */
if (customTimeInput) {
    customTimeInput.addEventListener("keydown", function (event) {
        if (event.key === "Enter") {
            event.preventDefault();
            addCustomTime();
        }
    });
}

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