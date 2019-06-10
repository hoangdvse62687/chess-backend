package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@Entity
@Table(name = "couse_has_interactive_lesson")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="courseHasInteractiveLessionId",scope = CouseHasInteractiveLesson.class)
public class CouseHasInteractiveLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long courseHasInteractiveLessionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="interactive_lesson_id")
    private InteractiveLesson interactiveLesson;

    public long getCourseHasInteractiveLessionId() {
        return courseHasInteractiveLessionId;
    }

    public void setCourseHasInteractiveLessionId(long courseHasInteractiveLessionId) {
        this.courseHasInteractiveLessionId = courseHasInteractiveLessionId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public InteractiveLesson getInteractiveLesson() {
        return interactiveLesson;
    }

    public void setInteractiveLesson(InteractiveLesson interactiveLesson) {
        this.interactiveLesson = interactiveLesson;
    }
}
