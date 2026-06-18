function shareCurrentPage() {
    const pageTitle = document.title || '롤면서살자';
    const pageUrl = window.location.href;

    const shareTitle = makeShareTitle(pageTitle);
    const shareText = makeShareText(pageTitle);
    const memberText = makeMemberShareText();

    let copyText = shareTitle + '\n\n' + shareText;

    if (memberText.length > 0) {
        copyText += '\n\n' + memberText;
    }

    copyText += '\n\n' + pageUrl;

    copyShareText(copyText);
}

function makeShareTitle(title) {
    const cleanTitle = title
        .replace('롤면서살자 |', '')
        .replace('롤면서살자', '')
        .trim();

    if (cleanTitle.length === 0) {
        return '[롤면서살자] 공유 링크';
    }

    return '[롤면서살자] ' + cleanTitle;
}

function makeShareText(title) {
    const cleanTitle = title
        .replace('롤면서살자 |', '')
        .replace('롤면서살자', '')
        .trim();

    if (cleanTitle.includes('신청 현황')) {
        return cleanTitle + '입니다.\n아래 링크에서 참여자 목록을 확인해주세요.';
    }

    return cleanTitle + ' 페이지입니다.\n아래 링크에서 확인해주세요.';
}

function makeMemberShareText() {
    const rows = document.querySelectorAll('.member-row');

    if (rows.length === 0) {
        return '현재 선택된 시간에는 참여자가 없습니다.';
    }

    let text = '현재 보이는 시간 기준 신청자:';

    rows.forEach(function (row) {
        const nickname = row.querySelector('.nickname')?.innerText.trim();
        const selectedLine = row.querySelector('.selected-line')?.innerText.trim();

        if (nickname) {
            text += '\n- ' + nickname;

            if (selectedLine) {
                text += ' / ' + selectedLine;
            }
        }
    });

    return text;
}

function copyShareText(text) {
    if (navigator.clipboard) {
        navigator.clipboard.writeText(text)
            .then(function () {
                alert('공유 내용이 복사되었습니다.\n카카오톡 채팅방에 붙여넣기 해주세요.');
            })
            .catch(function () {
                fallbackCopyShareText(text);
            });

        return;
    }

    fallbackCopyShareText(text);
}

function fallbackCopyShareText(text) {
    const textarea = document.createElement('textarea');

    textarea.value = text;
    textarea.style.position = 'fixed';
    textarea.style.left = '-9999px';
    textarea.style.top = '-9999px';

    document.body.appendChild(textarea);

    textarea.focus();
    textarea.select();

    try {
        document.execCommand('copy');
        alert('공유 내용이 복사되었습니다.\n카카오톡 채팅방에 붙여넣기 해주세요.');
    } catch (e) {
        alert('복사에 실패했습니다. 주소창의 링크를 직접 복사해주세요.');
    }

    document.body.removeChild(textarea);
}