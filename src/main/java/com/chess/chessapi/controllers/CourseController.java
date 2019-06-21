package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.constants.ObjectType;
import com.chess.chessapi.constants.Status;
import com.chess.chessapi.entities.*;
import com.chess.chessapi.exceptions.AccessDeniedException;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.*;
import com.chess.chessapi.utils.TimeUtils;
import com.chess.chessapi.viewmodels.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @ApiOperation(value = "Create course")
    @PostMapping("/create-course")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult createCourse(@Valid @RequestBody CourseCreateViewModel course, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            UserPrincipal userPrincipal = this.userService.getCurrentUser();
            try{
                Course savedCourse = this.courseService.create(course,userPrincipal.getId());
                for (Long categoryId:
                     course.getListCategoryIds()) {
                    this.categoryHasCourseService.create(categoryId,savedCourse.getCourseId());
                }
                //create mapping
                this.userHasCourseService.create(userPrincipal.getId(),savedCourse.getCourseId()
                        , TimeUtils.getCurrentTime(), Status.USER_HAS_COURSE_STATUS_IN_PROCESS);
                message =  AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.COURSE);
            }catch (Exception ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.COURSE);
                isSuccess = false;
            }
        }
        return new JsonResult(message,isSuccess);
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
            data = this.courseService.getCoursePaginationByStatusId(nameCourse,page,pageSize, statusId);
        }catch (IllegalArgumentException ex){
            throw new ResourceNotFoundException("Page","number",page);
        }

        return new JsonResult(null,data);
    }

    @ApiOperation(value = "remove course")
    @PutMapping("/remove-course")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult removeCourse(@RequestBody CourseRemoveViewModel courseRemoveViewModel){

        boolean hasPermissionModify = this.courseService.checkPermissionModifyCourse(courseRemoveViewModel.getCourseId());

        String message = "";
        Boolean isSuccess = true;
        try{
            if(hasPermissionModify){
                this.courseService.updateStatus(courseRemoveViewModel.getCourseId(),Status.COURSE_STATUS_REMOVED);
                message = AppMessage.getMessageSuccess(AppMessage.DELETE,AppMessage.COURSE);
            }else {
                throw new AccessDeniedException(AppMessage.PERMISSION_MESSAGE);
            }
        }catch (Exception ex){
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
            UserDetailViewModel author = this.courseService.getAuthor(course);

            String messageNotification = "";

            //handle status admin can change
            if(courseUpdateStatusViewModel.getStatusId() == Status.COURSE_STATUS_PUBLISHED){
                messageNotification = AppMessage.UPDATE_COURSE_STATUS_PUBLISHED;
            }else{
                messageNotification = AppMessage.UPDATE_COURSE_STATUS_REJECTED;
                courseUpdateStatusViewModel.setStatusId(Status.COURSE_STATUS_REJECTED);
            }

            if(author != null){
                //Send notification to author
                Notification notification = new Notification();
                notification.setObjectTypeId(ObjectType.COURSE);
                notification.setCreateDate(TimeUtils.getCurrentTime());
                notification.setContent(messageNotification);
                notification.setViewed(false);
                notification.setObjectId(course.getCourseId());
                notification.setUserId(author.getUserId());
                notification.setRoleTarget(AppRole.ROLE_INSTRUCTOR);
                notification.setObjectName(course.getName());
                this.notificationService.create(notification);
            }

            this.courseService.updateStatus(courseUpdateStatusViewModel.getCourseId(),courseUpdateStatusViewModel.getStatusId());
            message = AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.COURSE);
        }catch (Exception ex){
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
        this.courseService.getCourseDetails(course);
        return new JsonResult("",course);
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
                    throw new AccessDeniedException(AppMessage.PERMISSION_MESSAGE);
                }
            }catch (Exception ex){
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
            throw new AccessDeniedException(AppMessage.PERMISSION_MESSAGE);
        }
        return new JsonResult("",this.courseService.getCourseDetailsByLessonId(lessonId));
    }
}
