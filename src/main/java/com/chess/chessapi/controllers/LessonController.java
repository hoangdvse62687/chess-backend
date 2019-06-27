package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.entities.Lesson;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.exceptions.AccessDeniedException;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.CreateResponse;
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
import org.springframework.dao.DataIntegrityViolationException;
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
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_LEARNER_AUTHENTICATIION+","
            + AppRole.ROLE_ADMIN_AUTHENTICATIION+","
            + AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
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
        long savedId = 0;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            try{
                UserPrincipal userPrincipal = this.userService.getCurrentUser();
                savedId = this.lessonService.createInteractiveLesson(lessonViewModel,userPrincipal.getId());
                message =  AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.INTERACTIVE_LESSON);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.INTERACTIVE_LESSON);
                isSuccess = false;
            }
        }
        CreateResponse createResponse = new CreateResponse();
        createResponse.setSuccess(isSuccess);
        createResponse.setSavedId(savedId);
        return new JsonResult(message,createResponse);
    }

    @ApiOperation(value = "Update interactive lesson")
    @PutMapping(value = "/update-interactive-lesson")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult updateInteractiveLesson(@Valid @RequestBody InteractiveLessonUpdateViewModel interactiveLessonUpdateViewModel, BindingResult bindingResult){

        if(!this.lessonService.checkPermissionModifyLesson(interactiveLessonUpdateViewModel.getLessonId())){
            throw new AccessDeniedException(AppMessage.PERMISSION_MESSAGE);
        }


        if(!this.lessonService.isExist(interactiveLessonUpdateViewModel.getLessonId())){
            new ResourceNotFoundException("Lesson","id",interactiveLessonUpdateViewModel.getLessonId());
        }

        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            try{
                this.lessonService.updateInteractiveLesson(interactiveLessonUpdateViewModel);
                message =  AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.INTERACTIVE_LESSON);
            }catch (DataIntegrityViolationException ex){
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
        long savedId = 0;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            try{
                UserPrincipal userPrincipal = this.userService.getCurrentUser();
                savedId = this.lessonService.createUninteractiveLesson(lessonViewModel,userPrincipal.getId());
                message =  AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.UNINTERACTIVE_LESSON);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.UNINTERACTIVE_LESSON);
                isSuccess = false;
            }
        }
        CreateResponse createResponse = new CreateResponse();
        createResponse.setSavedId(savedId);
        createResponse.setSuccess(isSuccess);
        return new JsonResult(message,createResponse);
    }

    @ApiOperation(value = "Update uninteractive lesson")
    @PutMapping(value = "/update-uninteractive-lesson")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult updateUninteractiveLesson(@Valid @RequestBody UninteractiveLessonUpdateViewModel uninteractiveLessonUpdateViewModel, BindingResult bindingResult){

        if(!this.lessonService.checkPermissionModifyLesson(uninteractiveLessonUpdateViewModel.getLessonId())){
            throw new AccessDeniedException(AppMessage.PERMISSION_MESSAGE);
        }

        if(!this.lessonService.isExist(uninteractiveLessonUpdateViewModel.getLessonId())){
            new ResourceNotFoundException("Lesson","id",uninteractiveLessonUpdateViewModel.getLessonId());
        }

        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            try{
                this.lessonService.updateUninteractiveLesson(uninteractiveLessonUpdateViewModel);
                message =  AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.UNINTERACTIVE_LESSON);
            }catch (DataIntegrityViolationException ex){
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
        }catch (DataIntegrityViolationException ex){
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

        Lesson lesson = this.lessonService.getById(lessonRemoveViewModel.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson","id",lessonRemoveViewModel.getLessonId()));
        this.lessonService.removeLesson(lesson);

        Boolean isSuccess = true;
        String message = "";
        try{
            message = AppMessage.getMessageSuccess(AppMessage.DELETE,AppMessage.LESSON);
        }catch (DataIntegrityViolationException ex){
            isSuccess = false;
            message =  AppMessage.getMessageFail(AppMessage.DELETE,AppMessage.LESSON);
        }
        return new JsonResult(message,isSuccess);
    }
    @ApiOperation(value = "mapping lesson to course")
    @PutMapping(value = "/mapping-lesson-course")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult removeLesson(@RequestBody MappingLessonCourseViewModel mappingLessonCourseViewModel){
        if(!this.courseService.checkPermissionModifyCourse(mappingLessonCourseViewModel.getCourseId())){
            throw new AccessDeniedException(AppMessage.PERMISSION_MESSAGE);
        }

        if(!this.lessonService.checkPermissionModifyLesson(mappingLessonCourseViewModel.getLessonId())){
            throw new AccessDeniedException(AppMessage.PERMISSION_MESSAGE);
        }

        if(!this.lessonService.isExist(mappingLessonCourseViewModel.getLessonId())){
            new ResourceNotFoundException("Lesson","id",mappingLessonCourseViewModel.getLessonId());
        }

        if(!this.courseService.isExist(mappingLessonCourseViewModel.getCourseId())){
            new ResourceNotFoundException("Course","id",mappingLessonCourseViewModel.getCourseId());
        }

        Boolean isSuccess = true;
        String message = "";
        try{
            int lessonOrder = this.courseHasLessonService.getLastestLessonOrder(mappingLessonCourseViewModel.getCourseId());
            this.courseHasLessonService.create(mappingLessonCourseViewModel.getLessonId(),mappingLessonCourseViewModel.getCourseId(),lessonOrder);
            message = AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.LESSON);
        }catch (DataIntegrityViolationException ex){
            isSuccess = false;
            message =  AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.LESSON);
        }
        return new JsonResult(message,isSuccess);
    }
}
