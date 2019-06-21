package com.chess.chessapi.viewmodels;

import com.chess.chessapi.entities.InteractiveLesson;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class InteractiveLessonCreateViewModel {
    private long lessonId;
    @NotNull(message = "Name must not be null")
    private String name;

    private Timestamp createdDate;

    @NotNull(message = "Interactive Lesson must not be null")
    private InteractiveLesson interactiveLesson;

    private long courseId;

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

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
}
