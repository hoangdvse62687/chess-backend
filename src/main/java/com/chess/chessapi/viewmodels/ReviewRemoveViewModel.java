package com.chess.chessapi.viewmodels;

import javax.validation.constraints.NotNull;

public class ReviewRemoveViewModel {
    @NotNull(message = "Review id must not be null")
    private long reviewId;

    @NotNull(message = "Course id must not be null")
    private long courseId;

    public long getReviewId() {
        return reviewId;
    }

    public void setReviewId(long reviewId) {
        this.reviewId = reviewId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
}
