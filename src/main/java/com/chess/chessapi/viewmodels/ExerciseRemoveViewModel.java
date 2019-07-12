package com.chess.chessapi.viewmodels;

import javax.validation.constraints.NotNull;

public class ExerciseRemoveViewModel {
    @NotNull(message = "Exercise id must not be null")
    private long exerciseId;

    public long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(long exerciseId) {
        this.exerciseId = exerciseId;
    }
}
