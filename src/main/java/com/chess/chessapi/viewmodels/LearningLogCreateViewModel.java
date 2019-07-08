package com.chess.chessapi.viewmodels;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class LearningLogCreateViewModel {
    @NotNull(message = "Course id must not be null")
    private long courseId;

    @NotNull(message = "Lesson id must not be null")
    private long lessonId;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }
}
