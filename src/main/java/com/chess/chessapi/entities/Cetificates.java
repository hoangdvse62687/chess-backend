package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;

@Entity
@Table(name = "cetificates")
public class Cetificates {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String cetificate_link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fk_user")
    @JsonBackReference
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getCetificateLink() {
        return cetificate_link;
    }

    public void setCetificateLink(String cetificateLink) {
        this.cetificate_link = cetificateLink;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}