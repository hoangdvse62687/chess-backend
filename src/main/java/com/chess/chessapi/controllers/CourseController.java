package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.constants.ObjectType;
import com.chess.chessapi.constants.Status;
import com.chess.chessapi.entities.CategoryHasCourse;
import com.chess.chessapi.entities.Course;
import com.chess.chessapi.entities.Notification;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.*;
import com.chess.chessapi.utils.TimeUtils;
import com.chess.chessapi.viewmodels.CourseDetailViewModel;
import com.chess.chessapi.viewmodels.CoursePaginationViewModel;
import com.chess.chessapi.viewmodels.UserDetailViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @ApiOperation(value = "Create course")
    @PostMapping("/create-course")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public JsonResult createCourse(@Valid @RequestBody Course course, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            UserPrincipal userPrincipal = this.userService.getCurrentUser();
            try{
                Course savedCourse = this.courseService.create(course);
                for (Long categoryId:
                     course.getListCategoryIds()) {
                    this.categoryHasCourseService.create(categoryId,savedCourse.getCourseId());
                }
                //add author
                this.userHasCourseService.create(userPrincipal.getId(),savedCourse.getCourseId());
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
    public JsonResult getCoursePaginationByStatusId(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize
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
    public JsonResult removeCourse(@RequestParam("courseId") long courseId){
        //check current user has this course
        UserPrincipal userPrincipal = this.userService.getCurrentUser();

        User user = this.userService.getUserById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User","id",userPrincipal.getId()));
        user.setCourseDetailViewModels(this.userService.getCourseDetails(user.getUserId()));

        List<CourseDetailViewModel> courseDetailViewModels = user.getCourseDetailViewModels();
        boolean isExist = false;
        for (CourseDetailViewModel courseDetailViewModel:
             courseDetailViewModels) {
            if(courseDetailViewModel.getCourseId() == courseId){
                isExist = true;
                break;
            }
        }
        String message = "";
        Boolean isSuccess = true;
        try{
            if(isExist){
                this.courseService.updateStatus(courseId,Status.COURSE_STATUS_REMOVED);
                message = AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.COURSE);
            }else {
                isSuccess = false;
                message = AppMessage.PERMISSION_MESSAGE;
            }
        }catch (Exception ex){
            message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.COURSE);
            isSuccess = false;
        }

        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "")
    @PutMapping("/update-course-status")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_ADMIN_AUTHENTICATIION+")")
    public JsonResult updateCourseStatus(@RequestParam("courseId") long courseId,@RequestParam("statusId") long statusId){
        String message = "";
        Boolean isSuccess = true;
        try{
            //Get course modify
            Course course = this.courseService.getCourseById(courseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Course","id",courseId));
            UserDetailViewModel author = this.courseService.getAuthor(course);

            String messageNotification = "";

            //handle status admin can change
            if(statusId == Status.COURSE_STATUS_PUBLISHED){
                messageNotification = AppMessage.UPDATE_COURSE_STATUS_PUBLISHED;
            }else{
                messageNotification = AppMessage.UPDATE_COURSE_STATUS_REJECTED;
                statusId = Status.COURSE_STATUS_REJECTED;
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

            this.courseService.updateStatus(courseId,statusId);
            message = AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.COURSE);
        }catch (Exception ex){
            message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.COURSE);
            isSuccess = false;
        }

        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "get course detail by course id")
    @GetMapping("/get-course-detail-by-courseid")
    public JsonResult getCourseDetailByCourseId(@RequestParam("courseId") long courseId){
        Course course = this.courseService.getCourseById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course","id",courseId));
        this.courseService.getCourseDetails(course);
        return new JsonResult("",course);
    }
}
