package com.chess.chessapi.entities;

import com.chess.chessapi.viewmodels.CourseDetailViewModel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "interactive_lesson")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="interactiveLessonId",scope = InteractiveLesson.class)
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "getInteractiveLessonByCourseId",
                procedureName = "get_interactive_lesson_by_courseid",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "courseId",type = Long.class)
                }
        )
})
public class InteractiveLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long interactiveLessonId;

    @NotNull
    @Length(max = 1000,message = "name is required not large than 1000 characters")
    private String name;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "interactiveLesson")
    @JsonIgnore
    private List<CouseHasInteractiveLesson> couseHasInteractiveLessons;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "interactiveLesson")
    private List<Step> steps;

    @Transient
    private List<CourseDetailViewModel> courseDetailViewModels;

    public long getInteractiveLessonId() {
        return interactiveLessonId;
    }

    public void setInteractiveLessonId(long interactiveLessonId) {
        this.interactiveLessonId = interactiveLessonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CouseHasInteractiveLesson> getCouseHasInteractiveLessons() {
        return couseHasInteractiveLessons;
    }

    public void setCouseHasInteractiveLessons(List<CouseHasInteractiveLesson> couseHasInteractiveLessons) {
        this.couseHasInteractiveLessons = couseHasInteractiveLessons;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<CourseDetailViewModel> getCourseDetailViewModels() {
        return courseDetailViewModels;
    }

    public void setCourseDetailViewModels(List<CourseDetailViewModel> courseDetailViewModels) {
        this.courseDetailViewModels = courseDetailViewModels;
    }
}
