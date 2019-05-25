package com.chess.chessapi.entities;

import com.chess.chessapi.constant.AuthProvider;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Email
    @Length(max = 255,message = "Email shouldn't larger than 255 characters")
    @NotBlank(message = "Email is required not null")
    private String email;
    @NotBlank(message = "Full Name is required not null")
    @Length(max = 255,message = "Full name shouldn't larger than 255 characters")
    private String fullName;

    private int gender;

    @Length(max = 255,message = "Link avatar shouldn't larger than 255 characters")
    private String avatar;

    private java.sql.Timestamp createdDate;

    private long isActive;

    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Length(min = 8,max = 15,message = "Phone should in range between 8 and 15 characters")
    private String phone;

    @Length(max = 255,message = "District shouldn't larger than 255 characters")
    private String district;

    @Length(max = 255,message = "City shouldn't larger than 255 characters")
    private String city;

    private long point;

    @Length(max = 255,message = "Role shouldn't larger than 255 characters")
    private String role;

    @Length(max = 255,message = "Achievement shouldn't larger than 255 characters")
    private String achievement;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    @JsonManagedReference
    private Set<Cetificates> cetificates;

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
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public java.sql.Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(java.sql.Timestamp createdDate) {
        this.createdDate = createdDate;
    }


    public long getIsActive() {
        return isActive;
    }

    public void setIsActive(long isActive) {
        this.isActive = isActive;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
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

    public Set<Cetificates> getCetificates() {
        return cetificates;
    }

    public void setCetificates(Set<Cetificates> cetificates) {
        this.cetificates = cetificates;
    }
}
