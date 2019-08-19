package com.chess.chessapi.viewmodels;

import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ExerciseLessonCreateViewModel {
    @NotNull(message = "Name must not be null")
    @Length(min = 6,max = 1000,message = "Name is required in range 6 ~ 1000 characters")
    private String name;

    @Length(max = 1000,message = "Description is required not large than 1000 characters")
    private String description;

    @Valid
    @NotNull(message = "Exercise must not be null")
    private ExerciseCreateViewModel exerciseCreateViewModel;

    private long courseId;

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

    public ExerciseCreateViewModel getExerciseCreateViewModel() {
        return exerciseCreateViewModel;
    }

    public void setExerciseCreateViewModel(ExerciseCreateViewModel exerciseCreateViewModel) {
        this.exerciseCreateViewModel = exerciseCreateViewModel;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
}
