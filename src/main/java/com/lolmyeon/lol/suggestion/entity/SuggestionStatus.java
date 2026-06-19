package com.lolmyeon.lol.suggestion.entity;

public enum SuggestionStatus {

    RECEIVED("접수"),
    REVIEWING("검토중"),
    DONE("반영완료"),
    HOLD("보류");

    private final String label;

    SuggestionStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}