package com.chess.chessapi.viewmodels;

import java.sql.Timestamp;

public class CoursePaginationViewModel {
    private long courseId;
    private String courseName;
    private String courseDescription;
    private Timestamp courseCreatedDate;
    private Float point;
    private long statusId;
    private long authorId;
    private String authorName;

    public CoursePaginationViewModel() {

    }

    public CoursePaginationViewModel(long courseId, String courseName, String courseDescription, Timestamp courseCreatedDate, Float point, long statusId, long authorId, String authorName) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.courseCreatedDate = courseCreatedDate;
        this.point = point;
        this.statusId = statusId;
        this.authorId = authorId;
        this.authorName = authorName;
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

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
