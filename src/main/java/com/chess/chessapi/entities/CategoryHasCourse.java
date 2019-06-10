package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@Entity
@Table(name = "category_has_course")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="categoryHasCourseId",scope = CategoryHasCourse.class)
public class CategoryHasCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long categoryHasCourseId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="category_id")
    private Category category;

    public long getCategoryHasCourseId() {
        return categoryHasCourseId;
    }

    public void setCategoryHasCourseId(long categoryHasCourseId) {
        this.categoryHasCourseId = categoryHasCourseId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
