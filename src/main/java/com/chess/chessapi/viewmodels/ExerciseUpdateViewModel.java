package com.chess.chessapi.viewmodels;

import com.chess.chessapi.models.StepSuggest;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ExerciseUpdateViewModel {
    @NotNull(message = "Exercise id must not be null")
    private long exerciseId;

    @NotNull(message = "Name must not be null")
    @Length(max = 1000,message = "Name is required not larger than 1000 characters")
    private String name;

    @NotNull(message = "Question must not be null")
    @Length(min=6,max = 1000,message = "Question is required in range 6~1000 characters")
    private String question;

    @Column(name = "content",columnDefinition = "json")
    @NotNull(message = "Steps must not be null")
    private List<@NotNull(message = "Step Suggest must not be null") StepSuggest> answer = new ArrayList<StepSuggest>();

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

    public List<StepSuggest> getAnswer() {
        return answer;
    }

    public void setAnswer(List<StepSuggest> answer) {
        this.answer = answer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
