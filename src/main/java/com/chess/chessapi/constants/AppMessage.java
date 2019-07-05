package com.chess.chessapi.constants;

import java.io.Serializable;

public class AppMessage implements Serializable {
    //ACTION DEFINE
    public static final String UPDATE = "Update";
    public static final String CREATE = "Create";
    public static final String DELETE = "Delete";
    //END ACTION DEFINE

    //SPECIAL DEFINE
    public static final String USER = "user";
    public static final String PROFILE = "profile";
    public static final String COURSE = "course";
    public static final String CATEGORY = "category";
    public static final String LESSON = "lesson";
    public static final String INTERACTIVE_LESSON = "interactive lesson";
    public static final String UNINTERACTIVE_LESSON = "uninteractive lesson";
    public static final String LEARNING_LOG = "learning log";
    public static final String EXERCISE = "exercise";
    public static final String ENROLL = "enroll";
    public static final String RESOURCE_LINK = "resource link";
    public static final String REVIEW = "review";
    public static final String GAME_HISTORY = "game history";
    //END SPECIAL DEFINE

    //STATUS MESSAGE DEFINE
    public static final String SUCCESSFUL_MESSAGE = "successful";
    public static final String FAIL_MESSAGE = "fail";
    //END STATUS MESSAGE DEFINE

    //PERMISSION DEFINE
    public static final String PERMISSION_DENY_MESSAGE = "you don't have permission to do this action";
    public static final String POINT_DENY_MESSAGE = "your point currently don't have enough to do this action";
    //END PERMISION DEFINE

    //NOTIFICATION DEFINE
    public static final String CREATE_NEW_USER_AS_INSTRUCTOR = " is created as instructor role. Click to review its.";
    public static final String UPDATE_USER_STATUS_ACTIVE = " has been actived";
    public static final String UPDATE_USER_STATUS_INACTIVE = " has been inactived";
    public static final String CREATE_NEW_COURSE = " is waitting for you to accept";
    public static final String UPDATE_COURSE_STATUS_PUBLISHED = " is published";
    public static final String UPDATE_COURSE_STATUS_REJECTED = " is rejected";
    public static final String NOTIFICATION_REVIEW = " has reviewed your course";
    //END NOTIFICATION DEFINE

    public static String getMessageSuccess(String action,String table){
        return action + " " + table  + " " + SUCCESSFUL_MESSAGE;
    }

    public static String getMessageFail(String action,String table){
        return action + " " + table + " " + FAIL_MESSAGE;
    }
}
