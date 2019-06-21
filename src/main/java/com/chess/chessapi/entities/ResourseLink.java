package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@Entity
@Table(name = "resourse_link")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="resourseLinkId",scope = ResourseLink.class)
public class ResourseLink {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long resourseLinkId;

    private String link;

    private String position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="uilesson_id")
    @JsonIgnore
    private UninteractiveLesson uninteractiveLesson;

    public long getResourseLinkId() {
        return resourseLinkId;
    }

    public void setResourseLinkId(long resourseLinkId) {
        this.resourseLinkId = resourseLinkId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public UninteractiveLesson getUninteractiveLesson() {
        return uninteractiveLesson;
    }

    public void setUninteractiveLesson(UninteractiveLesson uninteractiveLesson) {
        this.uninteractiveLesson = uninteractiveLesson;
    }
}
