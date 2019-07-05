package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "exercise")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="exerciseId",scope = Exercise.class)
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long exerciseId;

    @NotNull(message = "Question must not be null")
    @Length(max = 1000,message = "Question is required not larger than 1000 characters")
    private String question;

    @NotNull(message = "Answer must not be null")
    @Length(max = 1000,message = "Answer is required not larger than 1000 characters")
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="course_id")
    @JsonIgnore
    private Course course;

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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
