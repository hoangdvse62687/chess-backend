package com.chess.chessapi.viewmodels;

import java.sql.Timestamp;
import java.util.List;

public class CourseDetailsViewModel {
    private long courseId;
    private String name;
    private String description;
    private Timestamp createdDate;
    private Float point;
    private Long statusId;
    private String image;
    private long authorId;
    private String authorName;
    private String authorAvatar;
    private List<UserDetailViewModel> userDetailViewModels;
    private List<Long> listCategoryIds;
    private List<Long> listLearningLogLessonIds;
    private List<LessonViewModel> lessonViewModels;
    private int totalLesson;
    private boolean isEnrolled;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<UserDetailViewModel> getUserDetailViewModels() {
        return userDetailViewModels;
    }

    public void setUserDetailViewModels(List<UserDetailViewModel> userDetailViewModels) {
        this.userDetailViewModels = userDetailViewModels;
    }

    public List<Long> getListCategoryIds() {
        return listCategoryIds;
    }

    public void setListCategoryIds(List<Long> listCategoryIds) {
        this.listCategoryIds = listCategoryIds;
    }

    public List<LessonViewModel> getLessonViewModels() {
        return lessonViewModels;
    }

    public void setLessonViewModels(List<LessonViewModel> lessonViewModels) {
        this.lessonViewModels = lessonViewModels;
    }

    public boolean isEnrolled() {
        return isEnrolled;
    }

    public void setEnrolled(boolean enrolled) {
        isEnrolled = enrolled;
    }

    public List<Long> getListLearningLogLessonIds() {
        return listLearningLogLessonIds;
    }

    public void setListLearningLogLessonIds(List<Long> listLearningLogLessonIds) {
        this.listLearningLogLessonIds = listLearningLogLessonIds;
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

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public int getTotalLesson() {
        return totalLesson;
    }

    public void setTotalLesson(int totalLesson) {
        this.totalLesson = totalLesson;
    }
}
