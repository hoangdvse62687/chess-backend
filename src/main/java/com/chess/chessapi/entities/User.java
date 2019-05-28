package com.chess.chessapi.entities;

import com.chess.chessapi.constant.AuthProvider;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Email
    @Length(max = 255,message = "Email shouldn't larger than 255 characters")
    @NotNull(message = "Email is required not null")
    private String email;

    @NotNull(message = "Full Name is required not null")
    @Length(max = 255,message = "Full name shouldn't larger than 255 characters")
    private String full_name;


    @Length(max = 255,message = "Link avatar shouldn't larger than 255 characters")
    private String avatar;

    private java.sql.Timestamp created_date;

    private int is_active;

    @Length(max = 255,message = "Role shouldn't larger than 255 characters")
    private String role;

    @Length(max = 255,message = "Achievement shouldn't larger than 255 characters")
    private String achievement;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
    @JsonManagedReference
    private List<Cetificates> cetificates;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getFullName() {
        return full_name;
    }

    public void setFullName(String fullName) {
        this.full_name = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public java.sql.Timestamp getCreatedDate() {
        return created_date;
    }

    public void setCreatedDate(java.sql.Timestamp createdDate) {
        this.created_date = createdDate;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public List<Cetificates> getCetificates() {
        return cetificates;
    }

    public void setCetificates(List<Cetificates> cetificates) {
        this.cetificates = cetificates;
    }
}
