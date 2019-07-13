package com.chess.chessapi.models;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class StepSuggest {
    @NotNull(message = "Content must not be null")
    @Length(max = 1000,message = "Content is required not large than 1000 characters")
    private String content;

    @NotNull(message = "Step code must not be null")
    @Length(max = 255,message = "step_code is required not large than 255 characters")
    private String stepCode;

    @NotNull(message = "Right response must not be null")
    @Length(max = 1000,message = "right_response is required not large than 255 characters")
    private String rightResponse;

    @NotNull(message = "Wrong response must not be null")
    @Length(max = 1000,message = "wrong_response is required not large than 255 characters")
    private String wrongResponse;

    private String suggest;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStepCode() {
        return stepCode;
    }

    public void setStepCode(String stepCode) {
        this.stepCode = stepCode;
    }

    public String getRightResponse() {
        return rightResponse;
    }

    public void setRightResponse(String rightResponse) {
        this.rightResponse = rightResponse;
    }

    public String getWrongResponse() {
        return wrongResponse;
    }

    public void setWrongResponse(String wrongResponse) {
        this.wrongResponse = wrongResponse;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }
}
