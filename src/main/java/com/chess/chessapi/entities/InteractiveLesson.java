package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "interactive_lesson")
public class InteractiveLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Length(max = 1000,message = "name is required not large than 1000 characters")
    private String name;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "interactiveLesson")
    @JsonManagedReference
    private List<CouseHasInteractiveLesson> couseHasInteractiveLessons;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "interactiveLesson")
    @JsonManagedReference
    private List<Step> steps;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
