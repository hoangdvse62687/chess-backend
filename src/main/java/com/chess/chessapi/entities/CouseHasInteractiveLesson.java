package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "couse_has_interactive_lesson")
public class CouseHasInteractiveLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="course_id")
    @JsonBackReference
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="interactive_lesson_id")
    @JsonBackReference
    private InteractiveLesson interactiveLesson;
}
