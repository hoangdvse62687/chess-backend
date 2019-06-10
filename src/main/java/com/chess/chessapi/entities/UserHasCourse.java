package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@Entity
@Table(name = "user_has_course")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="userHasCourseId",scope = UserHasCourse.class)
public class UserHasCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long userHasCourseId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="course_id")
    private Course course;

    public long getUserHasCourseId() {
        return userHasCourseId;
    }

    public void setUserHasCourseId(long userHasCourseId) {
        this.userHasCourseId = userHasCourseId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
