<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<style>
    .common-header-actions {
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .common-share-btn {
        width: 42px;
        height: 42px;
        border-radius: 14px;
        border: 1px solid rgba(250, 204, 21, 0.45);
        background: rgba(202, 138, 4, 0.18);
        color: #facc15;
        font-size: 13px;
        font-weight: 900;
        cursor: pointer;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        white-space: nowrap;
    }

    .common-share-btn:hover {
        background: rgba(202, 138, 4, 0.28);
        border-color: rgba(250, 204, 21, 0.8);
    }
</style>

<button type="button"
        class="common-share-btn"
        onclick="shareCurrentPage()">
    공유
</button>

<script src="<c:url value='/js/share.js' />"></script>