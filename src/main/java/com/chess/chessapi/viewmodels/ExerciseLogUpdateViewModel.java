package com.chess.chessapi.viewmodels;

import javax.validation.constraints.NotNull;

public class ExerciseLogUpdateViewModel {
    @NotNull(message = "Exercise log id is required must not be null")
    private long exerciseLogId;

    @NotNull(message = "Is Pass is required must not be null")
    private boolean isPassed;

    public long getExerciseLogId() {
        return exerciseLogId;
    }

    public void setExerciseLogId(long exerciseLogId) {
        this.exerciseLogId = exerciseLogId;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }
}
