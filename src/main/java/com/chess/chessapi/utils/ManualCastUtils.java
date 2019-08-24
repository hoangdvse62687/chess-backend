package com.chess.chessapi.utils;

import com.chess.chessapi.constants.Status;
import com.chess.chessapi.entities.*;
import com.chess.chessapi.models.Step;
import com.chess.chessapi.models.StepSuggest;
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
    private static final int COURSE_REQUIRED_POINT_INDEX = 7;
    private static final int COURSE_USERID_INDEX = 8;
    private static final int COURSE_USER_FULLNAME_INDEX = 9;
    private static final int COURSE_USER_AVATAR_INDEX = 10;
    private static final int COURSE_USER_IS_ENROLLED = 11;
    private static final int COURSE_CATEGORY_IDS= 12;
    private static final int COURSE_RATING_INDEX= 13;
    private static final int COURSE_TOTAL_RATING_INDEX= 14;
    private static final int COURSE_LEARNING_PROCESS_PERCENT = 15;
    //END COURSE DEFINED

    //LESSON DEFINED
    private static final int LESSON_ID_INDEX = 0;
    private static final int LESSON_NAME_INDEX = 1;
    private static final int LESSON_DESCRIPTION_INDEX = 2;
    private static final int LESSON_CREATED_DATE_INDEX = 3;
    private static final int LESSON_TYPE_INDEX = 4;
    private static final int LESSON_ORDER_LESSON_INDEX = 5;
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
    private static final int REVIEW_VIEW_MODEL_ID_INDEX = 0;
    private static final int REVIEW_VIEW_MODEL_RATING_INDEX = 1;
    private static final int REVIEW_VIEW_MODEL_CONTENT_INDEX = 2;
    private static final int REVIEW_VIEW_MODEL_USER_ID_INDEX = 3;
    private static final int REVIEW_VIEW_MODEL_CREATED_DATE_INDEX = 4;
    private static final int REVIEW_VIEW_MODEL_MODIFIED_DATE_INDEX = 5;
    private static final int REVIEW_VIEW_MODEL_USER_FULLNAME_INDEX = 6;
    private static final int REVIEW_VIEW_MODEL_EMAIL_INDEX = 7;
    private static final int REVIEW_VIEW_MODEL_USER_AVATAR_INDEX = 8;
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
    private static final char LEFT_BRACKETS = '(';
    private static final char RIGHT_BRACKETS = ')';
    //END JSON PARSER CHARACTER DEFINED
    //STEP JSON PARSER DEFINED
    private static final String STEP_JSON_PARSER_ID = "id";
    private static final String STEP_JSON_PARSER_MOVE = "move";
    private static final String STEP_JSON_PARSER_CONTENT = "content";
    private static final String STEP_JSON_PARSER_MOVE_DIRECTION = "moveDirection";
    private static final String STEP_JSON_PARSER_FEN= "fen";
    private static final String STEP_JSON_PARSER_PRE_ID= "preId";
    private static final String STEP_JSON_PARSER_ANSWERTYPE = "answerType";
    private static final String STEP_JSON_PARSER_ANSWERARR = "answerArr";
    private static final String STEP_JSON_PARSER_RIGHTRESPONSE = "rightResponse";
    private static final String STEP_JSON_PARSER_WRONGRRESPONSE = "wrongResponse";
    //END STEP JSON PARSER DEFINED

    //LEARNER STATUS PUBLISH COURSE REPORT
    private static final int LEARNER_STATUS_REPORT_COURSE_NAME_INDEX = 0;
    private static final int LEARNER_STATUS_REPORT_COURSE_STATUS_INDEX = 1;
    private static final int LEARNER_STATUS_REPORT_COURSE_IN_PROCESS = 2;
    private static final int LEARNER_STATUS_REPORT_COURSE_PASSED = 3;
    private static final int LEARNER_STATUS_REPORT_COURSE_NOT_PASSED = 4;
    //END LEARNER STATUS PUBLISH COURSE REPORT

    //NOTIFICATION DEFINED
    private static final int NOTIFICATION_ID_INDEX = 0;
    private static final int NOTIFICATION_OBJECT_ID_INDEX = 1;
    private static final int NOTIFICATION_OBJECT_NAME_INDEX = 2;
    private static final int NOTIFICATION_OBJECT_AVATAR_INDEX = 3;
    private static final int NOTIFICATION_OBJECT_TYPE_ID_INDEX = 4;
    private static final int NOTIFICATION_CONTENT_INDEX = 5;
    private static final int NOTIFICATION_IS_VIEWED_INDEX = 6;
    private static final int NOTIFICATION_CREATED_DATE_INDEX = 7;
    private static final int NOTIFICATION_ROLE_TARGET_INDEX = 8;
    private static final int NOTIFICATION_USER_ID_INDEX = 9;

    private static final String NOTIFICATION_ID = "notificationId";
    private static final String NOTIFICATION_OBJECT_ID_ = "objectId";
    private static final String NOTIFICATION_OBJECT_NAME = "objectName";
    private static final String NOTIFICATION_OBJECT_AVATAR = "objectAvatar";
    private static final String NOTIFICATION_OBJECT_TYPE_ID = "objectTypeId";
    private static final String NOTIFICATION_CONTENT = "content";
    private static final String NOTIFICATION_IS_VIEWED = "isViewed";
    private static final String NOTIFICATION_CREATED_DATE = "createDate";
    private static final String NOTIFICATION_ROLE_TARGET = "roleTarget";
    private static final String NOTIFICATION_TO = "to";
    private static final String NOTIFICATION = "notification";
    //END NOTIFICATION DEFINED

    //POINT LOG DEFINED
    public static final int POINT_LOG_CONTENT_INDEX = 0;
    public static final int POINT_LOG_POINT_INDEX = 1;
    public static final int POINT_LOG_CREATED_DATE_INDEX = 2;
    //END POINT LOG DEFINED

    //COURSE FOR NOTIFICATION DEFINED
    public static final int COURSE_FOR_NOTIFICATION_ID_INDEX = 0;
    public static final int COURSE_FOR_NOTIFICATION_NAME_INDEX = 1;
    public static final int COURSE_FOR_NOTIFICATION_IMAGE_INDEX = 2;
    //END COURSE FOR NOTIFICATION DEFINED

    //GAME HISTORY DEFINED
    public static final int GAMEHISTORY_VIEW_MODEL_ID_INDEX = 0;
    public static final int GAMEHISTORY_VIEW_MODEL_STARTTIME_INDEX = 1;
    public static final int GAMEHISTORY_VIEW_MODEL_LEVEL_INDEX = 2;
    public static final int GAMEHISTORY_VIEW_MODEL_GAMETIME_INDEX = 3;
    public static final int GAMEHISTORY_VIEW_MODEL_POINT_INDEX = 4;
    public static final int GAMEHISTORY_VIEW_MODEL_STATUS_INDEX = 5;
    //END GAME HISTORY DEFINED

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
        user.setActive(Integer.parseInt(data[USER_ISACTIVE_INDEX].toString()) == 1 ? true : false);
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

    public static List<GameHistoryViewModel> castPageObjectsToGameHistoryViewModel(Page<Object> objects)
            throws NumberFormatException{
        List<GameHistoryViewModel> gameHistoryViewModels = new ArrayList<>();
        for (Object object:
                objects.getContent()) {
            GameHistoryViewModel gameHistoryViewModel = new GameHistoryViewModel();
            Object[] data = (Object[]) object;
            gameHistoryViewModel.setGamehistoryId(Long.parseLong(data[GAMEHISTORY_VIEW_MODEL_ID_INDEX].toString()));
            gameHistoryViewModel.setStartTime(Timestamp.valueOf(data[GAMEHISTORY_VIEW_MODEL_STARTTIME_INDEX].toString()));
            gameHistoryViewModel.setGameTime(Integer.parseInt(data[GAMEHISTORY_VIEW_MODEL_GAMETIME_INDEX].toString()));
            gameHistoryViewModel.setLevel(Integer.parseInt(data[GAMEHISTORY_VIEW_MODEL_LEVEL_INDEX].toString()));
            gameHistoryViewModel.setPoint(Float.parseFloat(data[GAMEHISTORY_VIEW_MODEL_POINT_INDEX].toString()));
            gameHistoryViewModel.setStatus(Integer.parseInt(data[GAMEHISTORY_VIEW_MODEL_STATUS_INDEX].toString()));
            gameHistoryViewModels.add(gameHistoryViewModel);
        }
        return gameHistoryViewModels;
    }

    public static List<CoursePaginationViewModel> castListObjectToCourseFromGetCoursePaginations(List<Object[]> objects,List<Category> categories)
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
            coursePaginationViewModel.setRequiredPoint(Float.parseFloat(object[COURSE_REQUIRED_POINT_INDEX].toString()));

            UserDetailViewModel author = new UserDetailViewModel();
            author.setUserId(Long.parseLong(object[COURSE_USERID_INDEX].toString()));
            author.setFullName(object[COURSE_USER_FULLNAME_INDEX].toString());
            author.setAvatar(handleNullValueObject(object[COURSE_USER_AVATAR_INDEX],String.class));

            coursePaginationViewModel.setAuthor(author);
            coursePaginationViewModel.setEnrolled(parseBoolean(object[COURSE_USER_IS_ENROLLED].toString()));
            List<CategoryViewModel> categoryViewModels = new ArrayList<>();
            String[] categoryIds = handleNullValueObject(object[COURSE_CATEGORY_IDS],String.class).split(",");
            for (String categoryId:
                 categoryIds) {
                if(!categoryId.isEmpty()){
                    for (Category category:
                         categories) {
                        if(category.getCategoryId() == Long.parseLong(categoryId)){
                            CategoryViewModel categoryViewModel = new CategoryViewModel();
                            categoryViewModel.setCategoryId(category.getCategoryId());
                            categoryViewModel.setName(category.getName());
                            categoryViewModels.add(categoryViewModel);
                        }
                    }
                }
            }
            coursePaginationViewModel.setListCategorys(categoryViewModels);
            coursePaginationViewModel.setRating(Double.parseDouble(object[COURSE_RATING_INDEX].toString()));
            coursePaginationViewModel.setTotalRating(Long.parseLong(object[COURSE_TOTAL_RATING_INDEX].toString()));
            double learningProcessPercent = Double.parseDouble(object[COURSE_LEARNING_PROCESS_PERCENT].toString());
            if(learningProcessPercent > 1){
                coursePaginationViewModel.setLearningProcessPercent(100);
            }else{
                coursePaginationViewModel.setLearningProcessPercent((int)(learningProcessPercent * 100));
            }
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

    public static List<PointLogViewModel> castListObjectToPointLogPagination(List<Object[]> objects){
        List<PointLogViewModel> data = new ArrayList<>();
        for (Object[] object:
                objects) {
            PointLogViewModel pointLogViewModel = new PointLogViewModel();
            pointLogViewModel.setContent(object[POINT_LOG_CONTENT_INDEX].toString());
            pointLogViewModel.setPoint(Float.parseFloat(object[POINT_LOG_POINT_INDEX].toString()));
            pointLogViewModel.setCreatedDate(Timestamp.valueOf(object[POINT_LOG_CREATED_DATE_INDEX].toString()));
            data.add(pointLogViewModel);
        }
        return data;
    }

    public static List<LearnerStatusPublishCourseReportViewModel> castListObjectToLearnerStatusPublishCourseReport(List<Object[]> objects){
        List<LearnerStatusPublishCourseReportViewModel> data = new ArrayList<>();
        for (Object[] object:
                objects) {
            LearnerStatusPublishCourseReportViewModel learnerStatusPublishCourseReportViewModel =
                    new LearnerStatusPublishCourseReportViewModel();
            learnerStatusPublishCourseReportViewModel.setCourseName(object[LEARNER_STATUS_REPORT_COURSE_NAME_INDEX].toString());
            learnerStatusPublishCourseReportViewModel.setCourseStatus(Integer.parseInt(object[LEARNER_STATUS_REPORT_COURSE_STATUS_INDEX].toString()));
            learnerStatusPublishCourseReportViewModel.setCounterInProcessStatus(Integer.parseInt(object[LEARNER_STATUS_REPORT_COURSE_IN_PROCESS].toString()));
            learnerStatusPublishCourseReportViewModel.setCounterPassedStatus(Integer.parseInt(object[LEARNER_STATUS_REPORT_COURSE_PASSED].toString()));
            learnerStatusPublishCourseReportViewModel.setCounterNotPassedStatus(Integer.parseInt(object[LEARNER_STATUS_REPORT_COURSE_NOT_PASSED].toString()));
            data.add(learnerStatusPublishCourseReportViewModel);
        }
        return data;
    }

    public static List<Integer> castListObjectToListInteger(List<Object[]> objects){
        List<Integer> data = new ArrayList<>();
        for (Object[] object:
                objects) {
            for (Object item:
                    object) {
                data.add(Integer.parseInt(item.toString()));
            }
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
            lessonViewModel.setDescription(handleNullValueObject(object[LESSON_DESCRIPTION_INDEX],String.class));
            lessonViewModel.setCreatedDate(Timestamp.valueOf(object[LESSON_CREATED_DATE_INDEX].toString()));
            lessonViewModel.setLessonOrdered(Integer.parseInt(object[LESSON_ORDER_LESSON_INDEX].toString()));
            lessonViewModel.setLessonType(Integer.parseInt(object[LESSON_TYPE_INDEX].toString()));
            data.add(lessonViewModel);
        }
        return data;
    }

    public static List<LessonViewModel> castPageObjectToLessonViewModel(List<Object[]>  objects)
            throws NumberFormatException{
        List<LessonViewModel> result = new ArrayList<>();
        for (Object object:
                objects) {
            LessonViewModel lessonViewModel = new LessonViewModel();
            Object[] data = (Object[]) object;
            lessonViewModel.setLessonId(Long.parseLong(data[LESSON_ID_INDEX].toString()));
            lessonViewModel.setName(data[LESSON_NAME_INDEX].toString());
            lessonViewModel.setDescription(handleNullValueObject(data[LESSON_DESCRIPTION_INDEX],String.class));
            lessonViewModel.setCreatedDate(Timestamp.valueOf(data[LESSON_CREATED_DATE_INDEX].toString()));
            lessonViewModel.setLessonType(Integer.parseInt(data[LESSON_TYPE_INDEX].toString()));
            result.add(lessonViewModel);
        }
        return result;
    }

    public static List<Notification> castListObjectToNotification(List<Object> objects){
        List<Notification> result = new ArrayList<>();
        for (Object object:
                objects) {
            Notification notification = new Notification();
            Object[] data = (Object[]) object;
            notification.setNotificationId(Long.parseLong(data[NOTIFICATION_ID_INDEX].toString()));
            notification.setObjectId(Long.parseLong(data[NOTIFICATION_OBJECT_ID_INDEX].toString()));
            notification.setObjectName(data[NOTIFICATION_OBJECT_NAME_INDEX].toString());
            notification.setObjectAvatar(data[NOTIFICATION_OBJECT_AVATAR_INDEX].toString());
            notification.setObjectTypeId(Long.parseLong(data[NOTIFICATION_OBJECT_TYPE_ID_INDEX].toString()));
            notification.setContent(data[NOTIFICATION_CONTENT_INDEX].toString());
            notification.setViewed(data[NOTIFICATION_IS_VIEWED_INDEX].toString().equals("1") ? true : false);
            notification.setCreateDate(Timestamp.valueOf(data[NOTIFICATION_CREATED_DATE_INDEX].toString()));
            notification.setRoleTarget(Long.parseLong(data[NOTIFICATION_ROLE_TARGET_INDEX].toString()));
            result.add(notification);
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
            course.setRequiredPoint(courseCreateViewModel.getRequiredPoint());
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
        courseDetailsViewModel.setRequiredPoint(course.getRequiredPoint());
        UserDetailViewModel author = new UserDetailViewModel();
        author.setUserId(course.getUser().getUserId());
        author.setFullName(course.getUser().getFullName());
        author.setAvatar(course.getUser().getAvatar());

        courseDetailsViewModel.setAuthor(author);
        courseDetailsViewModel.setTotalLesson(totalLesson);
        courseDetailsViewModel.setListLogExerciseIds(course.getListLogExerciseIds());
        return courseDetailsViewModel;
    }

    public static User castUserUpdateToUser(UserUpdateViewModel userUpdateViewModel){
        User user = new User();
        if(userUpdateViewModel != null){
            user.setUserId(userUpdateViewModel.getUserId());
            user.setPoint(userUpdateViewModel.getPoint());
            user.setEmail(userUpdateViewModel.getEmail());
            user.setFullName(userUpdateViewModel.getFullName());
            user.setAvatar(userUpdateViewModel.getAvatar());
            user.setCreatedDate(userUpdateViewModel.getCreatedDate());
            user.setActive(userUpdateViewModel.isActive());
            user.setRoleId(userUpdateViewModel.getRoleId());
            user.setAchievement(userUpdateViewModel.getAchievement());
            user.setProvider(userUpdateViewModel.getProvider());
            user.setProviderId(userUpdateViewModel.getProviderId());
            List<Certificate> certificates = new ArrayList();
            for (CertificateUpdateViewModel c:
                 userUpdateViewModel.getCertificates()) {
                Certificate certificate = new Certificate();
                if(c.getCertificateId() != 0){
                    certificate.setCertificateId(c.getCertificateId());
                }
                certificate.setCertificateLink(c.getCertificateLink());
                certificates.add(certificate);
            }
            user.setCertificates(certificates);
        }
        return user;
    }

    public static List<ReviewPaginationViewModel> castListObjectToReviewFromGetReview(List<Object[]> objects)
    throws NumberFormatException{
        List<ReviewPaginationViewModel> data = new ArrayList<>();
        for (Object[] object:
                objects) {
            ReviewPaginationViewModel reviewPaginationViewModel = new ReviewPaginationViewModel();
            reviewPaginationViewModel.setReviewId(Long.parseLong(object[REVIEW_VIEW_MODEL_ID_INDEX].toString()));
            reviewPaginationViewModel.setRating(Integer.parseInt(object[REVIEW_VIEW_MODEL_RATING_INDEX].toString()));
            reviewPaginationViewModel.setContent(object[REVIEW_VIEW_MODEL_CONTENT_INDEX].toString());
            reviewPaginationViewModel.setCreatedDate(Timestamp.valueOf(object[REVIEW_VIEW_MODEL_CREATED_DATE_INDEX].toString()));
            if(object[REVIEW_VIEW_MODEL_MODIFIED_DATE_INDEX] != null){
                reviewPaginationViewModel.setModifiedDate(Timestamp.valueOf(object[REVIEW_VIEW_MODEL_MODIFIED_DATE_INDEX].toString()));
            }

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

    public static List<CourseForNotificationViewModel> castListObjectsToCourseForNotificationViewModel(List<Object[]> objects){
        List<CourseForNotificationViewModel> data = new ArrayList<>();
        for(Object[] object:
            objects){
            CourseForNotificationViewModel courseForNotificationViewModel = new CourseForNotificationViewModel();
            courseForNotificationViewModel.setCourseId(Long.parseLong(object[COURSE_FOR_NOTIFICATION_ID_INDEX].toString()));
            courseForNotificationViewModel.setCourseName(object[COURSE_FOR_NOTIFICATION_NAME_INDEX].toString());
            courseForNotificationViewModel.setCourseImage(object[COURSE_FOR_NOTIFICATION_IMAGE_INDEX].toString());

            data.add(courseForNotificationViewModel);
        }
        return data;
    }

    public static CourseForNotificationViewModel castObjectsToCourseForNotificationViewModel(Object objects){
        Object[] object = (Object[]) objects;
        if(object == null){
            return null;
        }
        CourseForNotificationViewModel courseForNotificationViewModel = new CourseForNotificationViewModel();
        courseForNotificationViewModel.setCourseId(Long.parseLong(object[COURSE_FOR_NOTIFICATION_ID_INDEX].toString()));
        courseForNotificationViewModel.setCourseName(object[COURSE_FOR_NOTIFICATION_NAME_INDEX].toString());
        courseForNotificationViewModel.setCourseImage(object[COURSE_FOR_NOTIFICATION_IMAGE_INDEX].toString());
        return courseForNotificationViewModel;
    }
    //END CAST OBJECT TO OBJECT DEFINED

    //CASTE OBJECT TO JSON DEFINED
    public static String castListStepToJson(List<Step> steps){
        if(steps == null || steps.isEmpty()){
            return LEFT_SQUARE_BRACKET + " " + RIGHT_SQUARE_BRACKET;
        }
        String result = "";
        for (Step step:
             steps) {
            result += LEFT_ANGLE_BRACKET + D_QUOT  + STEP_JSON_PARSER_ID + D_QUOT + COLON + D_QUOT +step.getId()+ D_QUOT
                    + COMMA + D_QUOT + STEP_JSON_PARSER_MOVE + D_QUOT + COLON + D_QUOT +step.getMove() + D_QUOT
                    + COMMA + D_QUOT + STEP_JSON_PARSER_CONTENT + D_QUOT + COLON + D_QUOT +step.getContent() + D_QUOT
                    + COMMA + D_QUOT + STEP_JSON_PARSER_MOVE_DIRECTION + D_QUOT + COLON + D_QUOT +step.getMoveDirection() + D_QUOT
                    + COMMA + D_QUOT + STEP_JSON_PARSER_FEN + D_QUOT + COLON + D_QUOT +step.getFen() + D_QUOT
                    + COMMA + D_QUOT + STEP_JSON_PARSER_PRE_ID + D_QUOT + COLON + D_QUOT +step.getPreId() + D_QUOT
                    + RIGHT_ANGLE_BRACKET + COMMA;
        }

        return LEFT_SQUARE_BRACKET + result.substring(0,result.length() - 1) + RIGHT_SQUARE_BRACKET;
    }

    public static String castAnswerToJson(ExerciseAnwserArray exerciseAnwserArray){
        if(exerciseAnwserArray == null){
            return LEFT_ANGLE_BRACKET + " " + RIGHT_ANGLE_BRACKET;
        }
        String result = "";
        String answerArr = "";
        for (List<StepSuggest> stepSuggest:
                exerciseAnwserArray.getAnswerArr()) {
            answerArr += castListStepSuggestToJson(stepSuggest);
        }

        answerArr = LEFT_SQUARE_BRACKET + answerArr + RIGHT_SQUARE_BRACKET;

        result += LEFT_ANGLE_BRACKET + D_QUOT  + STEP_JSON_PARSER_ANSWERARR + D_QUOT + COLON + answerArr
                + COMMA + D_QUOT + STEP_JSON_PARSER_ANSWERTYPE + D_QUOT + COLON + D_QUOT + exerciseAnwserArray.getAnswerType() + D_QUOT
                + COMMA + D_QUOT + STEP_JSON_PARSER_FEN + D_QUOT + COLON + D_QUOT + exerciseAnwserArray.getFen() + D_QUOT
                + RIGHT_ANGLE_BRACKET + COMMA;

        return result.substring(0,result.length() - 1);
    }

    public static String castToNotificationJson(Notification notification,String to){
        if(notification == null){
            return LEFT_ANGLE_BRACKET + " " + RIGHT_ANGLE_BRACKET;
        }
        String notificationJson = LEFT_ANGLE_BRACKET + D_QUOT  + NOTIFICATION_ID + D_QUOT + COLON + D_QUOT + notification.getNotificationId() + D_QUOT
                + COMMA + D_QUOT + NOTIFICATION_OBJECT_ID_ + D_QUOT + COLON + D_QUOT + notification.getObjectId() + D_QUOT
                + COMMA + D_QUOT + NOTIFICATION_OBJECT_NAME + D_QUOT + COLON + D_QUOT + notification.getObjectName() + D_QUOT
                + COMMA + D_QUOT + NOTIFICATION_OBJECT_AVATAR + D_QUOT + COLON + D_QUOT + notification.getObjectAvatar() + D_QUOT
                + COMMA + D_QUOT + NOTIFICATION_OBJECT_TYPE_ID + D_QUOT + COLON + D_QUOT + notification.getObjectTypeId() + D_QUOT
                + COMMA + D_QUOT + NOTIFICATION_CONTENT + D_QUOT + COLON + D_QUOT + notification.getContent() + D_QUOT
                + COMMA + D_QUOT + NOTIFICATION_IS_VIEWED + D_QUOT + COLON + D_QUOT + notification.isViewed()+ D_QUOT
                + COMMA + D_QUOT + NOTIFICATION_CREATED_DATE + D_QUOT + COLON + D_QUOT + notification.getCreateDate().toString() + D_QUOT
                + COMMA + D_QUOT + NOTIFICATION_ROLE_TARGET + D_QUOT + COLON + D_QUOT + notification.getRoleTarget() + D_QUOT
                + RIGHT_ANGLE_BRACKET;

        return LEFT_ANGLE_BRACKET + D_QUOT  + NOTIFICATION + D_QUOT + COLON + notificationJson
                + COMMA + D_QUOT + NOTIFICATION_TO + D_QUOT + COLON + D_QUOT + to + D_QUOT
                + RIGHT_ANGLE_BRACKET;
    }

    public static String castListStepSuggestToJson(List<StepSuggest> steps){
        if(steps == null || steps.isEmpty()){
            return LEFT_SQUARE_BRACKET + " " + RIGHT_SQUARE_BRACKET;
        }
        String result = "";
        for (StepSuggest step:
                steps) {
            result += LEFT_ANGLE_BRACKET + D_QUOT  + STEP_JSON_PARSER_ID + D_QUOT + COLON + D_QUOT +step.getId()+ D_QUOT
                    + COMMA + D_QUOT + STEP_JSON_PARSER_MOVE + D_QUOT + COLON + D_QUOT +step.getMove() + D_QUOT
                    + COMMA + D_QUOT + STEP_JSON_PARSER_CONTENT + D_QUOT + COLON + D_QUOT +step.getContent() + D_QUOT
                    + COMMA + D_QUOT + STEP_JSON_PARSER_MOVE_DIRECTION + D_QUOT + COLON + D_QUOT +step.getMoveDirection() + D_QUOT
                    + COMMA + D_QUOT + STEP_JSON_PARSER_FEN + D_QUOT + COLON + D_QUOT +step.getFen() + D_QUOT
                    + COMMA + D_QUOT + STEP_JSON_PARSER_PRE_ID + D_QUOT + COLON + D_QUOT +step.getPreId() + D_QUOT
                    + COMMA + D_QUOT + STEP_JSON_PARSER_RIGHTRESPONSE + D_QUOT + COLON + D_QUOT +step.getRightResponse() + D_QUOT
                    + COMMA + D_QUOT + STEP_JSON_PARSER_WRONGRRESPONSE + D_QUOT + COLON + D_QUOT +step.getWrongResponse() + D_QUOT
                    + RIGHT_ANGLE_BRACKET + COMMA;
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
