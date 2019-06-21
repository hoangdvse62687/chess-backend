package com.chess.chessapi.viewmodels;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class LessonViewModel {
    private long lessonId;
    @NotNull(message = "Name must not be null")
    private String name;
    private Timestamp createdDate;
    @NotNull(message = "Lesson order must not be null")
    private int lessonOrdered;

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

    public int getLessonOrdered() {
        return lessonOrdered;
    }

    public void setLessonOrdered(int lessonOrdered) {
        this.lessonOrdered = lessonOrdered;
    }
}
