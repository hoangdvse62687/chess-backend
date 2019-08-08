package com.chess.chessapi.viewmodels;

import com.chess.chessapi.models.Step;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class InteractiveLessonViewModel {
    @NotNull(message = "Init code must not be null")
    @Length(max = 1000,message = "InitCode is required not larger than 1000 characters")
    private String initCode;

    @NotNull(message = "Steps must not be null")
    private List<Step> steps = new ArrayList<Step>();

    public String getInitCode() {
        return initCode;
    }

    public void setInitCode(String initCode) {
        this.initCode = initCode;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
