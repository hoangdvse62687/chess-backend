package com.chess.chessapi.entities;

import com.chess.chessapi.models.Step;
import com.chess.chessapi.models.StepSuggest;
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
import java.util.ArrayList;
import java.util.List;

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
    private List<StepSuggest> answer = new ArrayList<StepSuggest>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="course_id")
    @JsonIgnore
    private Course course;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "exercise")
    @JsonIgnore
    private List<ExerciseLog> exerciseLogs;

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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<ExerciseLog> getExerciseLogs() {
        return exerciseLogs;
    }

    public void setExerciseLogs(List<ExerciseLog> exerciseLogs) {
        this.exerciseLogs = exerciseLogs;
    }
}
