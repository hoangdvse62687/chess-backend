package com.chess.chessapi.viewmodels;


import java.sql.Timestamp;

public class UserPagination {
    private long id;

    private String email;

    private String full_name;

    private String avatar;

    private java.sql.Timestamp created_date;

    private int is_active;

    private String role;

    public UserPagination() {
    }

    public UserPagination(long id, String email, String full_name, String avatar, Timestamp created_date, int is_active, String role) {
        this.id = id;
        this.email = email;
        this.full_name = full_name;
        this.avatar = avatar;
        this.created_date = created_date;
        this.is_active = is_active;
        this.role = role;
    }

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

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
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
}
