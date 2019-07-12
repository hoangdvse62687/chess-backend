package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_has_course")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="userHasCourseId",scope = UserHasCourse.class)
public class UserHasCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long userHasCourseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="course_id")
    @JsonIgnore
    private Course course;

    @Column(name = "enrolled_date")
    private Timestamp enrolledDate;

    @Column(name = "status_id")
    private long statusId;

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

    public Timestamp getEnroledDate() {
        return enrolledDate;
    }

    public void setEnroledDate(Timestamp enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }
}
