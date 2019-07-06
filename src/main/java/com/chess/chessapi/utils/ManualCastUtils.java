package com.chess.chessapi.utils;

import com.chess.chessapi.constants.Status;
import com.chess.chessapi.entities.*;
import com.chess.chessapi.models.Step;
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
    private static final int COURSE_USER_IS_ENROLLED = 10;
    //END COURSE DEFINED

    //LESSON DEFINED
    private static final int LESSON_ID_INDEX = 0;
    private static final int LESSON_NAME_INDEX = 1;
    private static final int LESSON_CREATED_DATE_INDEX = 2;
    private static final int LESSON_TYPE_INDEX = 3;
    private static final int LESSON_ORDER_LESSON_INDEX = 4;
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

    //REVIEW VIEW MODEL DEFINED
    private static final int REVIEW_VIEW_MODEL_RATING_INDEX = 0;
    private static final int REVIEW_VIEW_MODEL_CONTENT_INDEX = 1;
    private static final int REVIEW_VIEW_MODEL_USER_ID_INDEX = 2;
    private static final int REVIEW_VIEW_MODEL_CREATED_DATE_INDEX = 3;
    private static final int REVIEW_VIEW_MODEL_USER_FULLNAME_INDEX = 4;
    private static final int REVIEW_VIEW_MODEL_EMAIL_INDEX = 5;
    private static final int REVIEW_VIEW_MODEL_USER_AVATAR_INDEX = 6;
    //END REVIEW VIEW MODEL DEFINED

    //CATEGORY VIEW MODEL DEFINED
    private static final int CATEGORY_VIEW_MODEL_ID_INDEX = 0;
    private static final int CATEGORY_VIEW_MODEL_NAME_INDEX = 1;
    //END CATEGORY VIEW MODEL DEFINED

    //JSON PARSER CHARACTER DEFINED
    private static final char COLON = ':';
    private static final char LEFT_SQUARE_BRACKET = '[';
    private static final char RIGHT_SQUARE_BRACKET = ']';
    private static final String LEFT_ANGLE_BRACKET = "{";
    private static final char RIGHT_ANGLE_BRACKET = '}';
    private static final char D_QUOT = '"';
    private static final char COMMA = ',';
    //END JSON PARSER CHARACTER DEFINED
    //STEP JSON PARSER DEFINED
    private static final String STEP_JSON_PARSER_CONTENT = "content";
    private static final String STEP_JSON_PARSER_STEPCODE = "stepCode";
    private static final String STEP_JSON_PARSER_RIGHTRESPONSE = "rightResponse";
    private static final String STEP_JSON_PARSER_WRONGRRESPONSE = "wrongResponse";
    //END STEP JSON PARSER DEFINED


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

    public static CourseOverviewViewModel castObjectToCourseOverviewViewModel(List<Object> objects)
    throws NumberFormatException{
        if(objects == null){
            return null;
        }
        CourseOverviewViewModel courseOverviewViewModel = new CourseOverviewViewModel();
        Object[] data = (Object[]) objects.get(0);

        courseOverviewViewModel.setTotalQuantityRatings(Double.parseDouble(data[data.length - 2].toString()));

        for(int i = 0; i < data.length - 2;i++){
            OverviewRatingDetailsViewModel overviewRatingDetailsViewModel = new OverviewRatingDetailsViewModel();
            overviewRatingDetailsViewModel.setQuantity(Double.parseDouble(data[i].toString()));
            if(courseOverviewViewModel.getTotalQuantityRatings() != 0){
                overviewRatingDetailsViewModel.setRatio(overviewRatingDetailsViewModel.getQuantity()
                        / courseOverviewViewModel.getTotalQuantityRatings());
            }
            courseOverviewViewModel.getListRatings().add(overviewRatingDetailsViewModel);
        }
        courseOverviewViewModel.setTotalRatings(Float.parseFloat(handleNullValueObject(data[data.length - 1],Float.class)));
        return courseOverviewViewModel;
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

            UserDetailViewModel author = new UserDetailViewModel();
            author.setUserId(Long.parseLong(object[COURSE_USERID_INDEX].toString()));
            author.setFullName(object[COURSE_USER_FULLNAME_INDEX].toString());
            author.setAvatar(handleNullValueObject(object[COURSE_USER_AVATAR_INDEX],String.class));

            coursePaginationViewModel.setAuthor(author);
            coursePaginationViewModel.setEnrolled(parseBoolean(object[COURSE_USER_IS_ENROLLED].toString()));
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

    public static List<CategoryViewModel> castListObjectToCategoryIdFromGetCategoryByCourseId(List<Object[]> objects)
    throws NumberFormatException{
        List<CategoryViewModel> data = new ArrayList<>();
        for (Object[] object:
             objects) {
            CategoryViewModel categoryViewModel = new CategoryViewModel();
            categoryViewModel.setCategoryId(Long.parseLong(object[CATEGORY_VIEW_MODEL_ID_INDEX].toString()));
            categoryViewModel.setName(object[CATEGORY_VIEW_MODEL_NAME_INDEX].toString());
            data.add(categoryViewModel);
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
            lessonViewModel.setLessonType(Integer.parseInt(object[LESSON_TYPE_INDEX].toString()));
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
            lessonViewModel.setLessonType(Integer.parseInt(data[LESSON_TYPE_INDEX].toString()));
            result.add(lessonViewModel);
        }
        return result;
    }

    //CAST OBJECT TO OBJECT DEFINED
    public static Course castCourseCreateViewModelToCourse(CourseCreateViewModel courseCreateViewModel){
        Course course = new Course();
        if(courseCreateViewModel != null){
            course.setName(courseCreateViewModel.getName());
            course.setDescription(courseCreateViewModel.getDescription());
            course.setPoint(courseCreateViewModel.getPoint());
            course.setStatusId(Status.COURSE_STATUS_DRAFTED);
            course.setImage(courseCreateViewModel.getImage());
        }
        return course;
    }

    public static CourseDetailsViewModel castCourseToCourseDetailsViewModel(Course course,int totalLesson){
        CourseDetailsViewModel courseDetailsViewModel = new CourseDetailsViewModel();
        courseDetailsViewModel.setCourseId(course.getCourseId());
        courseDetailsViewModel.setName(course.getName());
        courseDetailsViewModel.setDescription(course.getDescription());
        courseDetailsViewModel.setCreatedDate(course.getCreatedDate());
        courseDetailsViewModel.setPoint(course.getPoint());
        courseDetailsViewModel.setStatusId(course.getStatusId());
        courseDetailsViewModel.setImage(course.getImage());
        courseDetailsViewModel.setUserEnrolleds(course.getUserEnrolleds());
        courseDetailsViewModel.setTutors(course.getTutors());
        courseDetailsViewModel.setListCategorys(course.getListCategorys());
        courseDetailsViewModel.setLessonViewModels(course.getLessonViewModels());
        courseDetailsViewModel.setListLearningLogLessonIds(course.getListLearningLogLessonIds());
        UserDetailViewModel author = new UserDetailViewModel();
        author.setUserId(course.getUser().getUserId());
        author.setFullName(course.getUser().getFullName());
        author.setAvatar(course.getUser().getAvatar());

        courseDetailsViewModel.setAuthor(author);
        courseDetailsViewModel.setTotalLesson(totalLesson);
        return courseDetailsViewModel;
    }

    public static List<ReviewPaginationViewModel> castListObjectToReviewFromGetReview(List<Object[]> objects)
    throws NumberFormatException{
        List<ReviewPaginationViewModel> data = new ArrayList<>();
        for (Object[] object:
                objects) {
            ReviewPaginationViewModel reviewPaginationViewModel = new ReviewPaginationViewModel();
            reviewPaginationViewModel.setRating(Integer.parseInt(object[REVIEW_VIEW_MODEL_RATING_INDEX].toString()));
            reviewPaginationViewModel.setContent(object[REVIEW_VIEW_MODEL_CONTENT_INDEX].toString());
            reviewPaginationViewModel.setCreatedDate(Timestamp.valueOf(object[REVIEW_VIEW_MODEL_CREATED_DATE_INDEX].toString()));

            UserDetailViewModel reviewer = new UserDetailViewModel();
            reviewer.setUserId(Long.parseLong(object[REVIEW_VIEW_MODEL_USER_ID_INDEX].toString()));
            reviewer.setEmail(object[REVIEW_VIEW_MODEL_EMAIL_INDEX].toString());
            reviewer.setFullName(object[REVIEW_VIEW_MODEL_USER_FULLNAME_INDEX].toString());
            reviewer.setAvatar(object[REVIEW_VIEW_MODEL_USER_AVATAR_INDEX].toString());
            reviewPaginationViewModel.setReviewer(reviewer);

            data.add(reviewPaginationViewModel);
        }
        return data;
    }
    //END CAST OBJECT TO OBJECT DEFINED

    //CASTE OBJECT TO JSON DEFINED
    public static String castListStepToJson(List<Step> steps){
        if(steps == null || steps.size() == 0){
            return "[]";
        }
        String result = "";
        for (Step step:
             steps) {
            result += LEFT_ANGLE_BRACKET + D_QUOT  + STEP_JSON_PARSER_CONTENT + D_QUOT + COLON + D_QUOT +step.getContent() + D_QUOT
                    + COMMA + D_QUOT + STEP_JSON_PARSER_STEPCODE + D_QUOT + COLON + D_QUOT +step.getStepCode() + D_QUOT
                    + COMMA + D_QUOT + STEP_JSON_PARSER_RIGHTRESPONSE + D_QUOT + COLON + D_QUOT +step.getRightResponse() + D_QUOT
                    + COMMA + D_QUOT + STEP_JSON_PARSER_WRONGRRESPONSE + D_QUOT + COLON + D_QUOT +step.getWrongResponse() + D_QUOT + RIGHT_ANGLE_BRACKET + COMMA;
        }

        return LEFT_SQUARE_BRACKET + result.substring(0,result.length() - 1) + RIGHT_SQUARE_BRACKET;
    }
    //END CASTE OBJECT TO JSON DEFINED
    //PRIVATE DEFINED
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

    private static boolean parseBoolean(String strBoolean){
        return strBoolean.equals("1") ? true : false;
    }
    //END PRIVATE DEFINED
}
