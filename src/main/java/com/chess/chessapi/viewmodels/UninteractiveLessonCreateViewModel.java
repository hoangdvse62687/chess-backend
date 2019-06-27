package com.chess.chessapi.viewmodels;

import com.chess.chessapi.entities.UninteractiveLesson;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class UninteractiveLessonCreateViewModel {
    @NotNull(message = "Name must not be null")
    private String name;

    @NotNull(message = "Uninteractive Lesson must not be null")
    private String content;

    private long courseId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
