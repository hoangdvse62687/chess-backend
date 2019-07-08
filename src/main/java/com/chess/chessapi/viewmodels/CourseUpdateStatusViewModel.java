package com.chess.chessapi.viewmodels;

import javax.validation.constraints.NotNull;

public class CourseUpdateStatusViewModel {
    @NotNull(message = "Course Id must not be null")
    private long courseId;
    @NotNull(message = "Status Id must not be null")
    private long statusId;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }
}
