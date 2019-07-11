package com.chess.chessapi.viewmodels;

import javax.validation.constraints.NotNull;

public class ExerciseLogCreateViewModel {
    @NotNull(message = "Exercise id is required must not be null")
    private long exerciseId;

    @NotNull(message = "Is Pass is required must not be null")
    private boolean isPassed;

    public long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }
}
