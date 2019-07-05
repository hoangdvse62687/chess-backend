package com.chess.chessapi.viewmodels;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class ReviewCreateViewModel {
    @NotNull(message = "Rating must not be null")
    private int rating;

    @Length(max = 1000,message = "name is required not large than 1000 characters")
    @NotNull(message = "Content must not be null")
    private String content;

    @NotNull(message = "Course id must not be null")
    private long courseId;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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
}
