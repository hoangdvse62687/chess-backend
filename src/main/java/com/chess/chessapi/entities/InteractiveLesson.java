package com.chess.chessapi.entities;

import com.chess.chessapi.models.Step;
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
@Table(name = "interactive_lesson")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="interactiveLessonId",scope = InteractiveLesson.class)
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class)
})
public class InteractiveLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long interactiveLessonId;

    @Column(name = "init_code")
    @NotNull(message = "Init code must not be null")
    @Length(max = 1000,message = "InitCode is required not larger than 1000 characters")
    private String initCode;

    @Type(type = "json")
    @Column(name = "content",columnDefinition = "json")
    @NotNull(message = "Steps must not be null")
    private List<@NotNull(message = "Step must not be null") Step> steps = new ArrayList<Step>();

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
