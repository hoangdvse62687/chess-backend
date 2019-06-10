package com.chess.chessapi.entities;

import com.chess.chessapi.viewmodels.InteractiveLessonViewModel;
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
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "status_id",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "role_id",type = Long.class),
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
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "categoryId",type = Long.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "getCourseByInteractiveId",
                procedureName = "get_courses_by_interactiveid",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "interactiveId",type = Long.class)
                }
        )
})
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long courseId;

    @NotNull
    @Length(max = 1000,message = "name is required not large than 1000 characters")
    private String name;

    private String description;

    @Column(name = "created_date")
    private Timestamp createdDate;

    private Float point;

    @Column(name = "status_id")
    private Long statusId;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "course")
    @JsonIgnore
    private List<UserHasCourse> userHasCourses;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "course")
    @JsonIgnore
    private List<CategoryHasCourse> categoryHasCourses;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "course")
    @JsonIgnore
    private List<CouseHasInteractiveLesson> couseHasInteractiveLessons;

    @Transient
    private List<UserDetailViewModel> userDetailViewModels;

    @Transient
    private List<Long> listCategoryIds;

    @Transient
    private List<InteractiveLessonViewModel> interactiveLessonViewModels;

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

    @JsonIgnore
    public List<CouseHasInteractiveLesson> getCouseHasInteractiveLessons() {
        return couseHasInteractiveLessons;
    }

    public void setCouseHasInteractiveLessons(List<CouseHasInteractiveLesson> couseHasInteractiveLessons) {
        this.couseHasInteractiveLessons = couseHasInteractiveLessons;
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

    public List<InteractiveLessonViewModel> getInteractiveLessonViewModels() {
        return interactiveLessonViewModels;
    }

    public void setInteractiveLessonViewModels(List<InteractiveLessonViewModel> interactiveLessonViewModels) {
        this.interactiveLessonViewModels = interactiveLessonViewModels;
    }
}
