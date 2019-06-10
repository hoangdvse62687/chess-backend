package com.chess.chessapi.utils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class TimeUtils implements Serializable {
    public static Timestamp getCurrentTime(){
        return new Timestamp(new Date().getTime());
    }
}
