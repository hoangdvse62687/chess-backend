package com.chess.chessapi.viewmodels;

import java.sql.Timestamp;

public class CoursePaginationViewModel {
    private long courseId;
    private String courseName;
    private String courseDescription;
    private String courseImage;
    private Timestamp courseCreatedDate;
    private Float point;
    private long statusId;
    private UserDetailViewModel author;
    private boolean isEnrolled;

    public CoursePaginationViewModel() {

    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public Timestamp getCourseCreatedDate() {
        return courseCreatedDate;
    }

    public void setCourseCreatedDate(Timestamp courseCreatedDate) {
        this.courseCreatedDate = courseCreatedDate;
    }

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    public String getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(String courseImage) {
        this.courseImage = courseImage;
    }

    public UserDetailViewModel getAuthor() {
        return author;
    }

    public void setAuthor(UserDetailViewModel author) {
        this.author = author;
    }

    public boolean isEnrolled() {
        return isEnrolled;
    }

    public void setEnrolled(boolean enrolled) {
        isEnrolled = enrolled;
    }
}
