package com.chess.chessapi.models;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

public class Step {
    @NotNull(message = "Content must not be null")
    @Length(max = 1000,message = "Content is required not large than 1000 characters")
    private String content;

    @NotNull(message = "Step code must not be null")
    @Length(max = 255,message = "step_code is required not large than 255 characters")
    private String stepCode;

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
}
