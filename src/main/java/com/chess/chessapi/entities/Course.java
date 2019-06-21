package com.chess.chessapi.entities;

import com.chess.chessapi.viewmodels.LessonViewModel;
import com.chess.chessapi.viewmodels.UserDetailViewModel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "course")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="courseId",scope = Course.class)
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "getCoursePaginations",
                procedureName = "get_course_paginations",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "courseName",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "pageIndex",type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "pageSize",type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "statusId",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.INOUT,name = "totalElements",type = Long.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "getCourseByUserId",
                procedureName = "get_courses_by_userid",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "userId",type = Long.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "getCourseByCategoryId",
                procedureName = "get_courses_by_categoryid",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "categoryId",type = Long.class),
                }
        ),
        @NamedStoredProcedureQuery(
                name = "getCoursesByLessonId",
                procedureName = "get_courses_by_lessonid",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "lessonId",type = Long.class),
                }
        ),
        @NamedStoredProcedureQuery(
                name = "checkPermissionUserCourse",
                procedureName = "check_permission_user_course",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "userId",type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "courseId",type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.INOUT,name = "hasPermission",type = Boolean.class)
                }
        )
})
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long courseId;

    @NotNull(message = "Name must not be null")
    @Length(max = 1000,message = "name is required not large than 1000 characters")
    private String name;

    private String description;

    @Column(name = "created_date")
    private Timestamp createdDate;

    private Float point;

    @Column(name = "status_id")
    private Long statusId;

    private String image;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "course")
    @JsonIgnore
    private List<UserHasCourse> userHasCourses;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "course")
    @JsonIgnore
    private List<CategoryHasCourse> categoryHasCourses;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "course")
    @JsonIgnore
    private List<CourseHasLesson> courseHasLessons;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "course")
    @JsonIgnore
    private List<LearningLog> learningLogs;

    @Transient
    private List<UserDetailViewModel> userDetailViewModels;

    @Transient
    private List<Long> listCategoryIds;

    @Transient
    private List<LessonViewModel> lessonViewModels;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owner")
    @JsonIgnore
    private User user;


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

    @JsonIgnore
    public List<UserHasCourse> getUserHasCourses() {
        return userHasCourses;
    }

    public void setUserHasCourses(List<UserHasCourse> userHasCourses) {
        this.userHasCourses = userHasCourses;
    }

    @JsonIgnore
    public List<CategoryHasCourse> getCategoryHasCourses() {
        return categoryHasCourses;
    }

    public void setCategoryHasCourses(List<CategoryHasCourse> categoryHasCourses) {
        this.categoryHasCourses = categoryHasCourses;
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

    public List<CourseHasLesson> getCourseHasLessons() {
        return courseHasLessons;
    }

    public void setCourseHasLessons(List<CourseHasLesson> courseHasLessons) {
        this.courseHasLessons = courseHasLessons;
    }

    public List<LessonViewModel> getLessonViewModels() {
        return lessonViewModels;
    }

    public void setLessonViewModels(List<LessonViewModel> lessonViewModels) {
        this.lessonViewModels = lessonViewModels;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<LearningLog> getLearningLogs() {
        return learningLogs;
    }

    public void setLearningLogs(List<LearningLog> learningLogs) {
        this.learningLogs = learningLogs;
    }
}
