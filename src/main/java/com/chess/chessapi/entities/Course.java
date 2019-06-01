package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Length(max = 1000,message = "name is required not large than 1000 characters")
    private String name;

    private String description;

    @Column(name = "created_date")
    private Timestamp createdDate;

    private Float point;

    private String status;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "course")
    @JsonManagedReference
    private List<UserHasCourse> userHasCourses;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "course")
    @JsonManagedReference
    private List<CategoryHasCourse> categoryHasCourses;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "course")
    @JsonManagedReference
    private List<CouseHasInteractiveLesson> couseHasInteractiveLessons;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<UserHasCourse> getUserHasCourses() {
        return userHasCourses;
    }

    public void setUserHasCourses(List<UserHasCourse> userHasCourses) {
        this.userHasCourses = userHasCourses;
    }

    public List<CategoryHasCourse> getCategoryHasCourses() {
        return categoryHasCourses;
    }

    public void setCategoryHasCourses(List<CategoryHasCourse> categoryHasCourses) {
        this.categoryHasCourses = categoryHasCourses;
    }

    public List<CouseHasInteractiveLesson> getCouseHasInteractiveLessons() {
        return couseHasInteractiveLessons;
    }

    public void setCouseHasInteractiveLessons(List<CouseHasInteractiveLesson> couseHasInteractiveLessons) {
        this.couseHasInteractiveLessons = couseHasInteractiveLessons;
    }
}
