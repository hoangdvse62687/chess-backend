package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.models.CreateResponse;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.ExerciseLogService;
import com.chess.chessapi.services.UserService;
import com.chess.chessapi.viewmodels.ExerciseLogCreateViewModel;
import com.chess.chessapi.viewmodels.LearningLogCreateViewModel;
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
@RequestMapping(value = "/exercise-log")
@Api(value = "Exercise Log Management")
public class ExerciseLogController {
    @Autowired
    private ExerciseLogService exerciseLogService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Create exercise log")
    @PostMapping(value = "/create-exercise-log")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody
    JsonResult createLearningLog(@Valid @RequestBody ExerciseLogCreateViewModel exerciseLogCreateViewModel, BindingResult bindingResult){
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
                savedId = this.exerciseLogService.create(exerciseLogCreateViewModel,userPrincipal.getId());
                message =  AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.EXERCISE_LOG);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.EXERCISE_LOG);
                isSuccess = false;
            }
        }
        CreateResponse createResponse = new CreateResponse();
        createResponse.setSavedId(savedId);
        createResponse.setSuccess(isSuccess);
        return new JsonResult(message,createResponse);
    }

    @ApiOperation(value = "get exercise log of current user by courseId")
    @GetMapping("/get-current-user-exercise-log-by-course-id")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody JsonResult getLearningLogByCourseId(@RequestParam("courseId") long courseId){
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        return new JsonResult(null,this.exerciseLogService.getAllExerciseIdsInLogByCourseIdAndUserId(userPrincipal.getId(),courseId));
    }
}
