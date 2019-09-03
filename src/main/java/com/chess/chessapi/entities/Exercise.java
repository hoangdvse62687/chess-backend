package com.chess.chessapi.entities;

import com.chess.chessapi.viewmodels.ExerciseAnswerArray;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "exercise")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="exerciseId",scope = Exercise.class)
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class)
})
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long exerciseId;

    @NotNull(message = "Question must not be null")
    @Length(max = 1000,message = "Question is required not larger than 1000 characters")
    private String question;

    @Type(type = "json")
    @Column(name = "answer",columnDefinition = "json")
    @NotNull(message = "Steps must not be null")
    private ExerciseAnswerArray answer = new ExerciseAnswerArray();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="lesson_id")
    @JsonIgnore
    private Lesson lesson;

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

    public ExerciseAnswerArray getAnswer() {
        return answer;
    }

    public void setAnswer(ExerciseAnswerArray answer) {
        this.answer = answer;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
