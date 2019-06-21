package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.entities.Lesson;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.exceptions.AccessDeniedException;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.CourseHasLessonService;
import com.chess.chessapi.services.CourseService;
import com.chess.chessapi.services.LessonService;
import com.chess.chessapi.services.UserService;
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
@RequestMapping(value = "/lesson")
@Api(value = "Lesson Management")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseHasLessonService courseHasLessonService;

    @ApiOperation(value = "get lesson detail by id")
    @GetMapping("/get-by-id")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody JsonResult getLessonDetailById(@RequestParam("lessonId") long lessonId){
        Lesson lesson = this.lessonService.getById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson","id",lessonId));
        return new JsonResult("",lesson);
    }

    @ApiOperation(value = "Create interactive lesson")
    @PostMapping(value = "/create-interactive-lesson")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult createInteractiveLesson(@Valid @RequestBody InteractiveLessonCreateViewModel lessonViewModel, BindingResult bindingResult){
        //if courseId are default => check permission create lesson on course
        if(lessonViewModel.getCourseId() != 0){
            if(!this.courseService.checkPermissionModifyCourse(lessonViewModel.getCourseId())){
                throw new AccessDeniedException(AppMessage.PERMISSION_MESSAGE);
            }
        }

        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            try{
                UserPrincipal userPrincipal = this.userService.getCurrentUser();
                this.lessonService.createInteractiveLesson(lessonViewModel,userPrincipal.getId());
                message =  AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.INTERACTIVE_LESSON);
            }catch (Exception ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.INTERACTIVE_LESSON);
                isSuccess = false;
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "Update interactive lesson")
    @PutMapping(value = "/update-interactive-lesson")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult updateInteractiveLesson(@Valid @RequestBody InteractiveLessonUpdateViewModel lessonViewModel, BindingResult bindingResult){

        if(!this.lessonService.checkPermissionModifyLesson(lessonViewModel.getLessonId())){
            throw new AccessDeniedException(AppMessage.PERMISSION_MESSAGE);
        }

        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            try{
                this.lessonService.updateInteractiveLesson(lessonViewModel);
                message =  AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.INTERACTIVE_LESSON);
            }catch (Exception ex){
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.INTERACTIVE_LESSON);
                isSuccess = false;
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "Create uninteractive lesson")
    @PostMapping(value = "/create-uninteractive-lesson")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult createUninteractiveLesson(@Valid @RequestBody UninteractiveLessonCreateViewModel lessonViewModel, BindingResult bindingResult){
        //if courseId are default => check permission create lesson on course
        if(lessonViewModel.getCourseId() != 0){
            if(!this.courseService.checkPermissionModifyCourse(lessonViewModel.getCourseId())){
                throw new AccessDeniedException(AppMessage.PERMISSION_MESSAGE);
            }
        }

        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            try{
                UserPrincipal userPrincipal = this.userService.getCurrentUser();
                this.lessonService.createUninteractiveLesson(lessonViewModel,userPrincipal.getId());
                message =  AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.UNINTERACTIVE_LESSON);
            }catch (Exception ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.UNINTERACTIVE_LESSON);
                isSuccess = false;
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "Update uninteractive lesson")
    @PutMapping(value = "/update-uninteractive-lesson")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult updateUninteractiveLesson(@Valid @RequestBody UninteractiveLessonUpdateViewModel lessonViewModel, BindingResult bindingResult){

        if(!this.lessonService.checkPermissionModifyLesson(lessonViewModel.getLessonId())){
            throw new AccessDeniedException(AppMessage.PERMISSION_MESSAGE);
        }

        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            try{
                this.lessonService.updateUninteractiveLesson(lessonViewModel);
                message =  AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.UNINTERACTIVE_LESSON);
            }catch (Exception ex){
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.UNINTERACTIVE_LESSON);
                isSuccess = false;
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "get lesson of current user")
    @GetMapping("/get-lesson-paginations-by-current-user")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult getLessonByOwner(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize,String name){
        if(name == null){
            name = "";
        }
        name = "%" + name + "%";
        PagedList<LessonViewModel> data = null;
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        try{
            data = this.lessonService.getAllByOwner(page,pageSize,name,userPrincipal.getId());
        }catch (IllegalArgumentException ex){
            throw new ResourceNotFoundException("Page","number",page);
        }
        return new JsonResult("",data);
    }

    @ApiOperation(value = "Remove lesson from course")
    @PutMapping(value = "/remove-lesson-course")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult removeLessonFromCourse(@RequestBody LessonCourseRemoveViewModel lessonCourseRemoveViewModel){
        if(!this.courseService.checkPermissionModifyCourse(lessonCourseRemoveViewModel.getCourseId())){
            throw new AccessDeniedException(AppMessage.PERMISSION_MESSAGE);
        }

        Boolean isSuccess = true;
        String message = "";
        try{
            this.courseHasLessonService.removeLessonFromCourse(lessonCourseRemoveViewModel.getLessonId(),lessonCourseRemoveViewModel.getCourseId());
            message = AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.LESSON);
        }catch (Exception ex){
            isSuccess = false;
            message =  AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.LESSON);
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "Remove lesson")
    @PutMapping(value = "/remove-lesson")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult removeLesson(@RequestBody LessonRemoveViewModel lessonRemoveViewModel){
        if(!this.lessonService.checkPermissionModifyLesson(lessonRemoveViewModel.getLessonId())){
            throw new AccessDeniedException(AppMessage.PERMISSION_MESSAGE);
        }

        Boolean isSuccess = true;
        String message = "";
        try{
            this.lessonService.removeLesson(lessonRemoveViewModel.getLessonId());
            message = AppMessage.getMessageSuccess(AppMessage.DELETE,AppMessage.LESSON);
        }catch (Exception ex){
            isSuccess = false;
            message =  AppMessage.getMessageFail(AppMessage.DELETE,AppMessage.LESSON);
        }
        return new JsonResult(message,isSuccess);
    }
}
