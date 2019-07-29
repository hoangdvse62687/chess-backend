package com.chess.chessapi.viewmodels;

import com.chess.chessapi.entities.InteractiveLesson;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class InteractiveLessonUpdateViewModel {
    private long lessonId;

    @NotNull(message = "Name must not be null")
    @Length(min = 6,max = 1000,message = "Name is required in range 6 ~ 1000 characters")
    private String name;

    private Timestamp createdDate;

    @Valid
    @NotNull(message = "Interactive Lesson must not be null")
    private InteractiveLesson interactiveLesson;

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public InteractiveLesson getInteractiveLesson() {
        return interactiveLesson;
    }

    public void setInteractiveLesson(InteractiveLesson interactiveLesson) {
        this.interactiveLesson = interactiveLesson;
    }
}
