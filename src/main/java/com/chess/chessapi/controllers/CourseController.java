package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.constants.ObjectType;
import com.chess.chessapi.constants.Status;
import com.chess.chessapi.entities.*;
import com.chess.chessapi.exceptions.AccessDeniedException;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.CreateResponse;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.*;
import com.chess.chessapi.utils.ManualCastUtils;
import com.chess.chessapi.utils.TimeUtils;
import com.chess.chessapi.viewmodels.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/course")
@Api(value = "Course Management")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CategoryHasCourseService categoryHasCourseService;

    @Autowired
    private UserHasCourseService userHasCourseService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LearningLogService learningLogService;

    @Autowired
    private CourseHasLessonService courseHasLessonService;

    @Autowired
    private ReviewService reviewService;

    @ApiOperation(value = "Create course")
    @PostMapping("/create-course")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult createCourse(@Valid @RequestBody CourseCreateViewModel course, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        long savedId = 0;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            UserPrincipal userPrincipal = this.userService.getCurrentUser();
            try{
                Course savedCourse = this.courseService.create(course,userPrincipal.getId());
                savedId = savedCourse.getCourseId();
                for (Long categoryId:
                     course.getListCategoryIds()) {
                    this.categoryHasCourseService.create(categoryId,savedCourse.getCourseId());
                }
                //create mapping
                this.userHasCourseService.create(userPrincipal.getId(),savedCourse.getCourseId()
                        , TimeUtils.getCurrentTime(), Status.USER_HAS_COURSE_STATUS_IN_PROCESS);
                message =  AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.COURSE);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.COURSE);
                isSuccess = false;
            }
        }
        CreateResponse createResponse = new CreateResponse();
        createResponse.setSavedId(savedId);
        createResponse.setSuccess(isSuccess);
        return new JsonResult(message,createResponse);
    }

    @ApiOperation(value = "Get course pagination")
    @GetMapping("/get-course-pagination")
    public @ResponseBody JsonResult getCoursePaginationByStatusId(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize
            ,String statusId,String nameCourse){
        if(nameCourse == null){
            nameCourse = "";
        }
        if(statusId == null){
            statusId = "";
        }
        PagedList<CoursePaginationViewModel> data = null;
        try{
            UserPrincipal userPrincipal = this.userService.getCurrentUser();
            long userId = 0;
            if(userPrincipal != null){
                userId = userPrincipal.getId();
            }
            data = this.courseService.getCoursePaginationByStatusId(nameCourse,page,pageSize, statusId,userId);
        }catch (IllegalArgumentException ex){
            throw new ResourceNotFoundException("Page","number",page);
        }

        return new JsonResult(null,data);
    }

    @ApiOperation(value = "remove course")
    @PutMapping("/remove-course")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult removeCourse(@RequestBody CourseRemoveViewModel courseRemoveViewModel){

        boolean hasPermissionModify = this.courseService.checkPermissionUpdateStatusCourse(courseRemoveViewModel.getCourseId());

        String message = "";
        Boolean isSuccess = true;
        try{
            if(hasPermissionModify){
                if(this.courseService.isExist(courseRemoveViewModel.getCourseId())){
                    this.courseService.updateStatus(courseRemoveViewModel.getCourseId(),Status.COURSE_STATUS_REMOVED);
                    message = AppMessage.getMessageSuccess(AppMessage.DELETE,AppMessage.COURSE);
                }else{
                    throw new ResourceNotFoundException("Course","id",courseRemoveViewModel.getCourseId());
                }
            }else {
                throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
            }
        }catch (DataIntegrityViolationException ex){
            message = AppMessage.getMessageFail(AppMessage.DELETE,AppMessage.COURSE);
            isSuccess = false;
        }

        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "update course status")
    @PutMapping("/update-course-status")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_ADMIN_AUTHENTICATIION+")")
    public @ResponseBody JsonResult updateCourseStatus(@RequestBody CourseUpdateStatusViewModel courseUpdateStatusViewModel){
        String message = "";
        Boolean isSuccess = true;

        try{
            //Get course modify
            Course course = this.courseService.getCourseById(courseUpdateStatusViewModel.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course","id",courseUpdateStatusViewModel.getCourseId()));

            String messageNotification = "";

            //handle status admin can change
            if(courseUpdateStatusViewModel.getStatusId() == Status.COURSE_STATUS_PUBLISHED){
                messageNotification = AppMessage.UPDATE_COURSE_STATUS_PUBLISHED;
            }else{
                messageNotification = AppMessage.UPDATE_COURSE_STATUS_REJECTED;
                courseUpdateStatusViewModel.setStatusId(Status.COURSE_STATUS_REJECTED);
            }

            //Send notification to author
            this.notificationService.sendNotificationToUser(messageNotification,course.getName(),ObjectType.COURSE,
                    course.getCourseId(),course.getUser().getUserId(),AppRole.ROLE_INSTRUCTOR);

            this.courseService.updateStatus(courseUpdateStatusViewModel.getCourseId(),courseUpdateStatusViewModel.getStatusId());
            message = AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.COURSE);
        }catch (DataIntegrityViolationException ex){
            message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.COURSE);
            isSuccess = false;
        }

        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "get course detail by course id")
    @GetMapping("/get-by-id")
    public @ResponseBody JsonResult getCourseDetailByCourseId(@RequestParam("courseId") long courseId){
        Course course = this.courseService.getCourseById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course","id",courseId));
        //get detail
        List<UserDetailViewModel> userDetailViewModels = this.userService.getUserDetailsByCourseId(course.getCourseId());
        course.setUserEnrolleds(this.userService.getUserEnrolls(userDetailViewModels));
        course.setTutors(this.userService.getTutors(userDetailViewModels));
        course.setListCategorys(this.categoryService.getListCategoryIdsByCourseId(course.getCourseId()));
        //check permission to get lesson and learning log
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        boolean permission = this.courseService.checkPermissionViewCourseDetail(courseId,userPrincipal);
        int totalLesson = 0;
        if(permission){
            course.setLessonViewModels(this.lessonService.getLessonByCourseId(course.getCourseId()));
            course.setListLearningLogLessonIds(this.learningLogService.getAllByCourseId(course.getCourseId(),userPrincipal.getId()));
            totalLesson = course.getLessonViewModels().size();
        }else{
            totalLesson = this.courseHasLessonService.countLessonByCourseId(course.getCourseId());
        }
        CourseDetailsViewModel courseDetailsViewModel = ManualCastUtils.castCourseToCourseDetailsViewModel(course,totalLesson);
        courseDetailsViewModel.setEnrolled(permission);
        return new JsonResult("", courseDetailsViewModel);
    }

    @ApiOperation(value = "update course")
    @PutMapping("/update-course")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult updateCourse(@Valid @RequestBody Course course, BindingResult bindingResult){
        boolean hasPermissionModify = this.courseService.checkPermissionModifyCourse(course.getCourseId());

        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            try{
                if(hasPermissionModify){
                    this.courseService.updateCourse(course);
                    message =  AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.COURSE);
                }else{
                    throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                }
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.COURSE);
                isSuccess = false;
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "get courses by lesson id")
    @GetMapping("/get-courses-by-lesson-id")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult getCoursesByLessonId(@RequestParam("lessonId") long lessonId){
        //check permission only author lesson can view course implement to lesson
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        long authorLesson = this.lessonService.getAuthorLessonByLessonId(lessonId);
        if(userPrincipal.getId() != authorLesson){
            throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
        }
        return new JsonResult("",this.courseService.getCourseDetailsByLessonId(lessonId));
    }

    @ApiOperation(value = "publish course")
    @PutMapping("/publish-course")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult publishCourse(@RequestBody CoursePublishViewModel coursePublishViewModel){
        String message = "";
        Boolean isSuccess = true;
        boolean hasPermissionModify = this.courseService.checkPermissionUpdateStatusCourse(coursePublishViewModel.getCourseId());
        try{

            //Get course modify
            Course course = this.courseService.getCourseById(coursePublishViewModel.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course","id",coursePublishViewModel.getCourseId()));
            UserPrincipal userPrincipal = this.userService.getCurrentUser();
            //check only author can update status
            if(hasPermissionModify){
                //Send notification to author
                //Send notification to Admin
                this.notificationService.sendNotificationToAdmin(AppMessage.CREATE_NEW_COURSE,course.getName(),
                        ObjectType.COURSE,course.getCourseId());
                this.courseService.updateStatus(coursePublishViewModel.getCourseId(),Status.COURSE_STATUS_WAITING);
                message = AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.COURSE);
            }else {
                throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
            }
        }catch (DataIntegrityViolationException ex){
            message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.COURSE);
            isSuccess = false;
        }

        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "Enroll course")
    @PostMapping("/enroll")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody JsonResult enrollCourse(@RequestBody @Valid EnrollCourseViewModel enrollCourseViewModel, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            Course course = this.courseService.getCourseById(enrollCourseViewModel.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course","id",enrollCourseViewModel.getCourseId()));
            UserPrincipal userPrincipal = this.userService.getCurrentUser();
            float calculatePointUpdate = this.userService.getPointByUserId(userPrincipal.getId());
            if(calculatePointUpdate < course.getPoint()){
                throw new AccessDeniedException(AppMessage.POINT_DENY_MESSAGE);
            }

            if(course.getStatusId() == Status.COURSE_STATUS_PUBLISHED){
                try {
                    this.userHasCourseService.create(userPrincipal.getId(),course.getCourseId()
                            , TimeUtils.getCurrentTime(),Status.USER_HAS_COURSE_STATUS_IN_PROCESS);
                    this.userService.increasePoint(userPrincipal.getId(),-course.getPoint());
                    message = AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.ENROLL);
                }catch (DataIntegrityViolationException ex){
                    message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.ENROLL);
                    isSuccess = false;
                }
            }else {
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.ENROLL);
                isSuccess = false;
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "get course paginations by category id")
    @GetMapping("/get-course-paginations-by-category-id")
    public @ResponseBody JsonResult getCoursePaginationByCourseId(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize,@RequestParam("categoryId") long categoryId){

        PagedList<CoursePaginationViewModel> data = null;
        try{
            UserPrincipal userPrincipal = this.userService.getCurrentUser();
            long userId = 0;
            if(userPrincipal != null){
                userId = userPrincipal.getId();
            }
            data = courseService.getCoursePaginationsByCategoryId(page,pageSize,categoryId,userId);
        }catch (IllegalArgumentException ex){
            throw new ResourceNotFoundException("Page","number",page);
        }

        return new JsonResult(null,data);
    }

    @ApiOperation(value = "Get review pagination")
    @GetMapping("/get-review-pagination")
    public @ResponseBody JsonResult getReviewPagination(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize
            ,@RequestParam("courseId") long courseId){
        return new JsonResult(null,this.reviewService.getReviewPaginationByCourse(page,pageSize,courseId));
    }

    @ApiOperation(value = "Get course overview")
    @GetMapping("/get-course-overview")
    public @ResponseBody JsonResult getCourseOverView(@RequestParam("courseId") long courseId){
        return new JsonResult(null,this.reviewService.getCourseOverview(courseId));
    }

    @ApiOperation(value = "Review on course")
    @PostMapping("/create-review")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_LEARNER_AUTHENTICATIION+","
            + AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult createReview(@RequestBody @Valid ReviewCreateViewModel reviewCreateViewModel,BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        long savedId = 0;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            try{
                UserPrincipal userPrincipal = this.userService.getCurrentUser();
                if(!this.courseService.checkPermissionReviewCourse(reviewCreateViewModel.getCourseId(),userPrincipal)){
                    throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                }
                savedId = this.reviewService.create(reviewCreateViewModel,userPrincipal.getId(),userPrincipal.getName());
                message =  AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.REVIEW);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.REVIEW);
                isSuccess = false;
            }
        }

        CreateResponse createResponse = new CreateResponse();
        createResponse.setSuccess(isSuccess);
        createResponse.setSavedId(savedId);
        return new JsonResult(message,createResponse);
    }

    @ApiOperation(value = "update review on course")
    @PutMapping("/update-review")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_LEARNER_AUTHENTICATIION+","
            + AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult updateReview(@RequestBody @Valid ReviewUpdateViewModel reviewUpdateViewModel,BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;

        if(!this.reviewService.isExist(reviewUpdateViewModel.getReviewId())){
            throw new ResourceNotFoundException("Review","id",reviewUpdateViewModel.getReviewId());
        }
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            try{
                UserPrincipal userPrincipal = this.userService.getCurrentUser();
                if(!this.reviewService.checkPermissionModifyReview(reviewUpdateViewModel.getReviewId()
                        ,userPrincipal.getId(),reviewUpdateViewModel.getCourseId())){
                    throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                }
                this.reviewService.update(reviewUpdateViewModel);
                message =  AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.REVIEW);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.REVIEW);
                isSuccess = false;
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "remove review on course")
    @PutMapping("/remove-review")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_LEARNER_AUTHENTICATIION+","
            + AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult removeReview(@RequestBody @Valid ReviewRemoveViewModel reviewRemoveViewModel,BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;

        if(!this.reviewService.isExist(reviewRemoveViewModel.getReviewId())){
            throw new ResourceNotFoundException("Review","id",reviewRemoveViewModel.getReviewId());
        }
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            try{
                UserPrincipal userPrincipal = this.userService.getCurrentUser();
                if(!this.reviewService.checkPermissionModifyReview(reviewRemoveViewModel.getReviewId()
                        ,userPrincipal.getId(),reviewRemoveViewModel.getCourseId())){
                    throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                }
                this.reviewService.remove(reviewRemoveViewModel.getReviewId());
                message =  AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.REVIEW);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.REVIEW);
                isSuccess = false;
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "Get course by current instructor")
    @GetMapping("/get-course-paginations-current-instructor")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult getCoursePaginationsByUserId(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize
            ,String nameCourse){
        if(nameCourse == null){
            nameCourse = "";
        }
        PagedList<CoursePaginationViewModel> data = null;
        try{
            UserPrincipal userPrincipal = this.userService.getCurrentUser();
            data = this.courseService.getCoursePaginationsByUserId(nameCourse,page,pageSize,userPrincipal.getId());
        }catch (IllegalArgumentException ex){
            throw new ResourceNotFoundException("Page","number",page);
        }

        return new JsonResult(null,data);
    }
}
