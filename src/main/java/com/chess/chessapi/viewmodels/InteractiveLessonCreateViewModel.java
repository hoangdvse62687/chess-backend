package com.chess.chessapi.viewmodels;

import com.chess.chessapi.entities.InteractiveLesson;

import javax.validation.constraints.NotNull;

public class InteractiveLessonCreateViewModel {
    @NotNull(message = "Name must not be null")
    private String name;

    @NotNull(message = "Interactive Lesson must not be null")
    private InteractiveLesson interactiveLesson;

    private long courseId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
