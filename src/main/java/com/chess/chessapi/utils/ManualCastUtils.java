package com.chess.chessapi.utils;

import com.chess.chessapi.entities.Course;
import com.chess.chessapi.entities.InteractiveLesson;
import com.chess.chessapi.entities.Step;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.viewmodels.*;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ManualCastUtils implements Serializable {
    //USER DEFINED
    private static final int USER_ID_INDEX = 0;
    private static final int USER_EMAIL_INDEX = 1;
    private static final int USER_ROLE_INDEX = 2;
    private static final int USER_ISACTIVE_INDEX = 3;
    private static final int USER_AVATAR_INDEX = 4;
    private static final int USER_FULLNAME_INDEX = 5;
    private static final int USER_CREATEDDATE_INDEX = 6;
    //END USER DEFINED

    //COURSE DEFINED
    private static final int COURSE_ID_INDEX = 0;
    private static final int COURSE_NAME_INDEX = 1;
    private static final int COURSE_STATUS_ID_INDEX = 2;
    private static final int COURSE_IMAGE_INDEX = 3;
    private static final int COURSE_DESCRIPTION_INDEX = 4;
    private static final int COURSE_CREATED_DATE_INDEX = 5;
    private static final int COURSE_POINT_INDEX = 6;
    private static final int COURSE_USERID_INDEX = 7;
    private static final int COURSE_USER_FULLNAME_INDEX = 8;
    private static final int COURSE_USER_AVATAR_INDEX = 9;
    //END COURSE DEFINED

    //LESSON DEFINED
    private static final int LESSON_ID_INDEX = 0;
    private static final int LESSON_NAME_INDEX = 1;
    private static final int LESSON_CREATED_DATE_INDEX = 2;
    private static final int LESSON_ORDER_LESSON_INDEX = 3;
    //END LESSON DEFINED

    //NULL VALUE DEFINED
    private static final String BOOLEAN_DEFAULT = "false";
    private static final String STRING_DEFAULT = "";
    private static final String NUMBER_DEFAULT = "0";
    //END NULL VALUE DEFINED

    //COURSE VIEW MODEL DEFINED
    private static final int COURSE_VIEW_MODEL_ID_INDEX = 0;
    private static final int COURSE_VIEW_MODEL_NAME_INDEX = 1;
    private static final int COURSE_VIEW_MODEL_STATUS_ID_INDEX = 2;
    private static final int COURSE_VIEW_MODEL_IMAGE_INDEX = 3;
    private static final int COURSE_VIEW_MODEL_CREATED_DATE_INDEX = 4;
    private static final int COURSE_VIEW_MODEL_AUTHOR_NAME_INDEX = 5;
    //END COURSE VIEW MODEL DEFINED

    //CAST STORED PROCEDURE DEFINED
    public static User castObjectToUserByFindCustom(Object object)
            throws NumberFormatException{
        if(object == null){
            return null;
        }
        User user = new User();
        Object[] data = (Object[]) object;
        user.setUserId(Long.parseLong(data[USER_ID_INDEX].toString()));
        user.setEmail(data[USER_EMAIL_INDEX].toString());
        user.setRoleId(Long.parseLong(data[USER_ROLE_INDEX].toString()));
        user.setActive(Boolean.parseBoolean(data[USER_ISACTIVE_INDEX].toString()));
        return user;
    }

    public static List<UserPaginationViewModel> castPageObjectsToUser(Page<Object> objects)
            throws NumberFormatException{
        List<UserPaginationViewModel> users = new ArrayList<>();
        for (Object object:
             objects.getContent()) {
            UserPaginationViewModel user = new UserPaginationViewModel();
            Object[] data = (Object[]) object;
            user.setUserId(Long.parseLong(data[USER_ID_INDEX].toString()));
            user.setEmail(data[USER_EMAIL_INDEX].toString());
            user.setRoleId(Integer.parseInt(data[USER_ROLE_INDEX].toString()));
            user.setIsActive(Integer.parseInt(data[USER_ISACTIVE_INDEX].toString()));
            user.setAvatar(data[USER_AVATAR_INDEX].toString());
            user.setFullName(data[USER_FULLNAME_INDEX].toString());
            user.setCreatedDate(Timestamp.valueOf(data[USER_CREATEDDATE_INDEX].toString()));
            users.add(user);
        }
        return users;
    }

    public static List<CoursePaginationViewModel> castListObjectToCourseFromGetCoursePaginations(List<Object[]> objects)
    throws NumberFormatException{
        List<CoursePaginationViewModel> data = new ArrayList<>();
        for (Object[] object:
             objects) {
            CoursePaginationViewModel coursePaginationViewModel = new CoursePaginationViewModel();
            coursePaginationViewModel.setCourseId(Long.parseLong(object[COURSE_ID_INDEX].toString()));
            coursePaginationViewModel.setCourseName(object[COURSE_NAME_INDEX].toString());
            coursePaginationViewModel.setStatusId(Long.parseLong(object[COURSE_STATUS_ID_INDEX].toString()));
            coursePaginationViewModel.setCourseImage(handleNullValueObject(object[COURSE_IMAGE_INDEX],String.class));
            coursePaginationViewModel.setCourseDescription(handleNullValueObject(object[COURSE_DESCRIPTION_INDEX],String.class));
            coursePaginationViewModel.setCourseCreatedDate(Timestamp.valueOf(object[COURSE_CREATED_DATE_INDEX].toString()));
            coursePaginationViewModel.setPoint(Float.parseFloat(object[COURSE_POINT_INDEX].toString()));
            coursePaginationViewModel.setAuthorId(Long.parseLong(object[COURSE_USERID_INDEX].toString()));
            coursePaginationViewModel.setAuthorName(object[COURSE_USER_FULLNAME_INDEX].toString());
            coursePaginationViewModel.setAuthorAvatar(handleNullValueObject(object[COURSE_USER_AVATAR_INDEX],String.class));
            data.add(coursePaginationViewModel);
        }
        return data;
    }

    public static List<CourseDetailViewModel> castListObjectToCourseDetailsFromGetCourseByCategoryId(List<Object[]> objects)
            throws NumberFormatException{
        List<CourseDetailViewModel> data = new ArrayList<>();
        for (Object[] object:
                objects) {
            CourseDetailViewModel courseDetailViewModel = new CourseDetailViewModel();
            courseDetailViewModel.setCourseId(Long.parseLong(object[COURSE_VIEW_MODEL_ID_INDEX].toString()));
            courseDetailViewModel.setName(object[COURSE_VIEW_MODEL_NAME_INDEX].toString());
            courseDetailViewModel.setStatusId(Long.parseLong(object[COURSE_VIEW_MODEL_STATUS_ID_INDEX].toString()));
            courseDetailViewModel.setImage(handleNullValueObject(object[COURSE_VIEW_MODEL_IMAGE_INDEX],String.class));
            courseDetailViewModel.setCreatedDate(Timestamp.valueOf(object[COURSE_VIEW_MODEL_CREATED_DATE_INDEX].toString()));
            courseDetailViewModel.setAuthorName(object[COURSE_VIEW_MODEL_AUTHOR_NAME_INDEX].toString());
            data.add(courseDetailViewModel);
        }
        return data;
    }

    public static List<CourseDetailViewModel> castListObjectToCourseDetails(List<Object[]> objects)
    throws NumberFormatException{
        List<CourseDetailViewModel> data = new ArrayList<>();
        for (Object[] object:
             objects) {
            CourseDetailViewModel courseDetailViewModel = new CourseDetailViewModel();
            courseDetailViewModel.setCourseId(Long.parseLong(object[COURSE_VIEW_MODEL_ID_INDEX].toString()));
            courseDetailViewModel.setName(object[COURSE_VIEW_MODEL_NAME_INDEX].toString());
            courseDetailViewModel.setStatusId(Long.parseLong(object[COURSE_VIEW_MODEL_STATUS_ID_INDEX].toString()));
            courseDetailViewModel.setImage(handleNullValueObject(object[COURSE_VIEW_MODEL_IMAGE_INDEX],String.class));
            data.add(courseDetailViewModel);
        }
        return data;
    }

    public static List<UserDetailViewModel> castListObjectToUserDetailsFromGetUsersByCourseid(List<Object[]> objects){
        List<UserDetailViewModel> data = new ArrayList<>();
        for (Object[] object:
                objects) {
            UserDetailViewModel userDetailViewModel = new UserDetailViewModel();
            userDetailViewModel.setUserId(Long.parseLong(object[USER_ID_INDEX].toString()));
            userDetailViewModel.setEmail(object[USER_EMAIL_INDEX].toString());
            userDetailViewModel.setRoleId(Integer.parseInt(object[USER_ROLE_INDEX].toString()));
            data.add(userDetailViewModel);
        }
        return data;
    }

    public static List<Long> castListObjectToCategoryIdFromGetCategoryByCourseId(List<Object[]> objects)
    throws NumberFormatException{
        List<Long> data = new ArrayList<>();
        for (Object[] object:
             objects) {
            data.add(Long.parseLong(object[0].toString()));
        }
        return data;
    }

    public static List<LessonViewModel> castListObjectToLessonViewModel(List<Object[]> objects)
    throws NumberFormatException{
        List<LessonViewModel> data = new ArrayList<>();
        for (Object[] object:
                objects) {
            LessonViewModel lessonViewModel = new LessonViewModel();
            lessonViewModel.setLessonId(Long.parseLong(object[LESSON_ID_INDEX].toString()));
            lessonViewModel.setName(object[LESSON_NAME_INDEX].toString());
            lessonViewModel.setCreatedDate(Timestamp.valueOf(object[LESSON_CREATED_DATE_INDEX].toString()));
            lessonViewModel.setLessonOrdered(Integer.parseInt(object[LESSON_ORDER_LESSON_INDEX].toString()));
            data.add(lessonViewModel);
        }
        return data;
    }

    public static List<LessonViewModel> castPageObjectToLessonViewModel(Page<Object> objects)
            throws NumberFormatException{
        List<LessonViewModel> result = new ArrayList<>();
        for (Object object:
                objects) {
            LessonViewModel lessonViewModel = new LessonViewModel();
            Object[] data = (Object[]) object;
            lessonViewModel.setLessonId(Long.parseLong(data[LESSON_ID_INDEX].toString()));
            lessonViewModel.setName(data[LESSON_NAME_INDEX].toString());
            lessonViewModel.setCreatedDate(Timestamp.valueOf(data[LESSON_CREATED_DATE_INDEX].toString()));
            result.add(lessonViewModel);
        }
        return result;
    }
    //END CAST STORED PROCEDURE DEFINED

    //CAST OBJECT TO OBJECT DEFINED
    public static Course castCourseCreateViewModelToCourse(CourseCreateViewModel courseCreateViewModel){
        Course course = new Course();
        if(courseCreateViewModel != null){
            course.setName(courseCreateViewModel.getName());
            course.setDescription(courseCreateViewModel.getDescription());
            course.setCreatedDate(courseCreateViewModel.getCreatedDate());
            course.setPoint(courseCreateViewModel.getPoint());
            course.setStatusId(courseCreateViewModel.getStatusId());
            course.setImage(courseCreateViewModel.getImage());
        }
        return course;
    }
    //END CAST OBJECT TO OBJECT DEFINED

    private static String handleNullValueObject(Object object,Class clazz){
        if(object != null){
            return object.toString();
        }else if(clazz == String.class){
            return STRING_DEFAULT;
        }else if(clazz == Boolean.class){
            return BOOLEAN_DEFAULT;
        }else{
            return NUMBER_DEFAULT;
        }
    }
}
