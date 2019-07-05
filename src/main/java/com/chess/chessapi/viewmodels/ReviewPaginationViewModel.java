package com.chess.chessapi.viewmodels;

public class ReviewPaginationViewModel {
    private int rating;

    private String content;

    private long userId;

    private String userFullName;

    private String userEmail;

    public void setRating(int rating) {
        this.rating = rating;
    }

    public float getRating() {
        return rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
