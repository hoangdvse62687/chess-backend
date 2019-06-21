package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "uninteractive_lesson")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="uninteractiveLessonId",scope = UninteractiveLesson.class)
public class UninteractiveLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long uninteractiveLessonId;

    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="lesson_id")
    @JsonIgnore
    private Lesson lesson;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "uninteractiveLesson")
    private List<ResourseLink> resourseLinks;

    public long getUninteractiveLessonId() {
        return uninteractiveLessonId;
    }

    public void setUninteractiveLessonId(long uninteractiveLessonId) {
        this.uninteractiveLessonId = uninteractiveLessonId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public List<ResourseLink> getResourseLinks() {
        return resourseLinks;
    }

    public void setResourseLinks(List<ResourseLink> resourseLinks) {
        this.resourseLinks = resourseLinks;
    }
}
