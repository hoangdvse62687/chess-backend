package com.chess.chessapi.constants;

import java.io.Serializable;

public class AppMessage implements Serializable {
    //ACTION DEFINE
    public static final String UPDATE = "Update";
    public static final String CREATE = "Create";
    //END ACTION DEFINE

    //SPECIAL DEFINE
    public static final String USER = "user";
    public static final String PROFILE = "profile";
    public static final String COURSE = "course";
    //END SPECIAL DEFINE

    //STATUS MESSAGE DEFINE
    public static final String SUCCESSFUL_MESSAGE = "successful";
    public static final String FAIL_MESSAGE = "fail";
    //END STATUS MESSAGE DEFINE

    //PERMISSION DEFINE
    public static final String PERMISSION_MESSAGE = "you don't have permission to this action";
    //END PERMISION DEFINE

    //NOTIFICATION DEFINE
    public static final String CREATE_NEW_USER_AS_INSTRUCTOR = " is created as instructor role. Click to review its.";
    public static final String UPDATE_USER_STATUS_ACTIVE = " has been actived";
    public static final String UPDATE_USER_STATUS_INACTIVE = " has been inactived";
    public static final String CREATE_NEW_COURSE = " is waitting for you to accept";
    public static final String UPDATE_COURSE_STATUS_PUBLISHED = " is published";
    public static final String UPDATE_COURSE_STATUS_REJECTED = " is rejected";
    //END NOTIFICATION DEFINE

    public static String getMessageSuccess(String action,String table){
        return action + " " + table  + " " + SUCCESSFUL_MESSAGE;
    }

    public static String getMessageFail(String action,String table){
        return action + " " + table + " " + FAIL_MESSAGE;
    }
}
