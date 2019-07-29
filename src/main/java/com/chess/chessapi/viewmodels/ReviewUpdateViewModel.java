package com.chess.chessapi.viewmodels;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class ReviewUpdateViewModel {
    @NotNull(message = "Review id must not be null")
    private long reviewId;

    @NotNull(message = "Content must not be null")
    @Length(min = 6,max = 1000,message = "name is required in range 6 ~ 1000 characters")
    private String content;

    private int rating;

    @NotNull(message = "Course id must not be null")
    private long courseId;

    public long getReviewId() {
        return reviewId;
    }

    public void setReviewId(long reviewId) {
        this.reviewId = reviewId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
