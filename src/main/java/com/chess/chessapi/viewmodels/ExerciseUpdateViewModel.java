package com.chess.chessapi.viewmodels;

import javax.validation.constraints.NotNull;

public class ExerciseUpdateViewModel {
    @NotNull(message = "Exercise id must not be null")
    private long exerciseId;
    @NotNull(message = "Question must not be null")
    private String question;
    @NotNull(message = "Answer must not be null")
    private String answer;

    public long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
