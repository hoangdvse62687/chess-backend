package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "resource_link")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="resourceLinkId",scope = ResourceLink.class)
public class ResourceLink {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long resourceLinkId;

    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owner")
    @JsonIgnore
    private User user;

    @Column(name = "created_date")
    private Timestamp createdDate;

    public long getResourceLinkId() {
        return resourceLinkId;
    }

    public void setResourceLinkId(long resourceLinkId) {
        this.resourceLinkId = resourceLinkId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}
