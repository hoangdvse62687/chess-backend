package com.chess.chessapi.viewmodels;

import com.chess.chessapi.entities.UninteractiveLesson;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class UninteractiveLessonUpdateViewModel {
    private long lessonId;
    @NotNull(message = "Name must not be null")
    @Length(min = 6,max = 1000,message = "Name is required in range 6 ~ 1000 characters")
    private String name;

    @NotNull(message = "Uninteractive Lesson must not be null")
    @Valid
    private UninteractiveLesson uninteractiveLesson;

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

    public UninteractiveLesson getUninteractiveLesson() {
        return uninteractiveLesson;
    }

    public void setUninteractiveLesson(UninteractiveLesson uninteractiveLesson) {
        this.uninteractiveLesson = uninteractiveLesson;
    }
}
