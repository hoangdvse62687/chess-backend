package com.chess.chessapi.viewmodels;

import com.chess.chessapi.models.Step;
import com.chess.chessapi.models.StepSuggest;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ExerciseCreateViewModel {

    @NotNull(message = "Question must not be null")
    @Length(min=6,max = 1000,message = "Question is required in range 6~1000 characters")
    private String question;

    @NotNull(message = "Answer must not be null")
    @Valid
    private ExerciseAnwserArray answer = new ExerciseAnwserArray();


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ExerciseAnwserArray getAnswer() {
        return answer;
    }

    public void setAnswer(ExerciseAnwserArray answer) {
        this.answer = answer;
    }
}
