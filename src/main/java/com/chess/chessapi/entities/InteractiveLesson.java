package com.chess.chessapi.entities;

import com.chess.chessapi.viewmodels.CourseDetailViewModel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "interactive_lesson")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="interactiveLessonId",scope = InteractiveLesson.class)
public class InteractiveLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long interactiveLessonId;

    @Column(name = "init_code")
    private String initCode;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "interactiveLesson")
    private List<Step> steps;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="lesson_id")
    @JsonIgnore
    private Lesson lesson;

    public long getInteractiveLessonId() {
        return interactiveLessonId;
    }

    public void setInteractiveLessonId(long interactiveLessonId) {
        this.interactiveLessonId = interactiveLessonId;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public String getInitCode() {
        return initCode;
    }

    public void setInitCode(String initCode) {
        this.initCode = initCode;
    }
}
