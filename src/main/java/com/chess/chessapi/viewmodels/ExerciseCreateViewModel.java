package com.chess.chessapi.viewmodels;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class ExerciseCreateViewModel {
    @NotNull(message = "Question must not be null")
    @Length(max = 1000,message = "Question is required not larger than 1000 characters")
    private String question;

    @NotNull(message = "Answer must not be null")
    @Length(max = 1000,message = "Answer is required not larger than 1000 characters")
    private String answer;

    @NotNull(message = "Course id must not be null")
    private long courseId;

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

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
}
