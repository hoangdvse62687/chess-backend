package com.chess.chessapi.constants;

import java.io.Serializable;

public class AppMessage implements Serializable {
    public static final String UPDATE = "Update";

    public static final String USER = "user";
    public static final String PROFILE = "profile";

    public static final String SUCCESSFUL_MESSAGE = "successful";
    public static final String FAIL_MESSAGE = "fail";

    public static final String PERMISSION_MESSAGE = "you don't have permission to this action";
    public static String getMessageSuccess(String action,String table,String field,String value){
        return action + " " + table + " " + field + " " + SUCCESSFUL_MESSAGE + " " + value;
    }

    public static String getMessageFail(String action,String table,String field,String value){
        return action + " " + table + " " + field + " " + FAIL_MESSAGE + " " + value;
    }

    public static String getMessageSuccess(String action,String table){
        return action + " " + table  + " " + SUCCESSFUL_MESSAGE;
    }

    public static String getMessageFail(String action,String table){
        return action + " " + table + " " + FAIL_MESSAGE;
    }
}
