package com.chess.chessapi.viewmodels;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class GameHistoryCreateViewModel {
    @NotNull(message = "Start time must not be null")
    @Column(name = "start_time")
    private Timestamp startTime;

    @NotNull(message = "Record must not be null")
    @Length(max = 3000, message = "record shouldn't larger than 3000 characters")
    private String record;

    @NotNull(message = "Level must not be null")
    private int level;

    @NotNull(message = "Color must not be null")
    private int color;

    @NotNull(message = "Game time must not be null")
    @Column(name = "game_time")
    private int gameTime;

    @NotNull(message = "Point must not be null")
    private int point;

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
