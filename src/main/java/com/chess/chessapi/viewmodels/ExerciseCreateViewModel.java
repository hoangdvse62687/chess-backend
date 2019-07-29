package com.chess.chessapi.viewmodels;

import com.chess.chessapi.models.Step;
import com.chess.chessapi.models.StepSuggest;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ExerciseCreateViewModel {
    @NotNull(message = "Question must not be null")
    @Length(min=6,max = 1000,message = "Question is required in range 6~1000 characters")
    private String question;

    @Column(name = "content",columnDefinition = "json")
    @NotNull(message = "Steps must not be null")
    private List<StepSuggest> answer = new ArrayList<StepSuggest>();

    @NotNull(message = "Course id must not be null")
    private long courseId;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<StepSuggest> getAnswer() {
        return answer;
    }

    public void setAnswer(List<StepSuggest> answer) {
        this.answer = answer;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
}
