package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "lesson")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="lessonId",scope = Lesson.class)
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "getLessonByCourseId",
                procedureName = "get_lesson_by_courseid",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "courseId",type = Long.class)
                }
        )
})
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long lessonId;

    @NotNull(message = "Name must not be null")
    @Length(max = 1000,message = "name is required not large than 1000 characters")
    private String name;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @OneToOne(mappedBy = "lesson",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private InteractiveLesson interactiveLesson;

    @OneToOne(mappedBy = "lesson",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private UninteractiveLesson uninteractiveLesson;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "lesson")
    @JsonIgnore
    private List<CourseHasLesson> courseHasLessons;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "lesson")
    @JsonIgnore
    private List<LearningLog> learningLogs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owner")
    @JsonIgnore
    private User user;

    @Column(name = "type")
    private int lessonType;

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public List<CourseHasLesson> getCourseHasLessons() {
        return courseHasLessons;
    }

    public void setCourseHasLessons(List<CourseHasLesson> courseHasLessons) {
        this.courseHasLessons = courseHasLessons;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public InteractiveLesson getInteractiveLesson() {
        return interactiveLesson;
    }

    public void setInteractiveLesson(InteractiveLesson interactiveLesson) {
        this.interactiveLesson = interactiveLesson;
    }

    public UninteractiveLesson getUninteractiveLesson() {
        return uninteractiveLesson;
    }

    public void setUninteractiveLesson(UninteractiveLesson uninteractiveLesson) {
        this.uninteractiveLesson = uninteractiveLesson;
    }

    public List<LearningLog> getLearningLogs() {
        return learningLogs;
    }

    public void setLearningLogs(List<LearningLog> learningLogs) {
        this.learningLogs = learningLogs;
    }

    public int getLessonType() {
        return lessonType;
    }

    public void setLessonType(int lessonType) {
        this.lessonType = lessonType;
    }
}
