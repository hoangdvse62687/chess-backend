package com.chess.chessapi.entities;

import com.chess.chessapi.constant.AuthProvider;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

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

    private int gender;

    @Length(max = 255,message = "Link avatar shouldn't larger than 255 characters")
    private String avatar;

    private java.sql.Timestamp created_date;

    private long is_active;

    @Pattern(regexp="([0-9]{8,15})",message = "Phone should in range between 8 and 15 characters")
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
        return full_name;
    }

    public void setFullName(String fullName) {
        this.full_name = fullName;
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
        return created_date;
    }

    public void setCreatedDate(java.sql.Timestamp createdDate) {
        this.created_date = createdDate;
    }


    public long getIsActive() {
        return is_active;
    }

    public void setIsActive(long isActive) {
        this.is_active = isActive;
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
