package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.entities.Exercise;
import com.chess.chessapi.exceptions.AccessDeniedException;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.CreateResponse;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.CourseService;
import com.chess.chessapi.services.ExerciseService;
import com.chess.chessapi.services.UserService;
import com.chess.chessapi.viewmodels.ExerciseCreateViewModel;
import com.chess.chessapi.viewmodels.ExerciseRemoveViewModel;
import com.chess.chessapi.viewmodels.ExerciseUpdateViewModel;
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
@RequestMapping(value = "/exercise")
@Api(value = "Exercise Management")
public class ExerciseController {
    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Create exercise")
    @PostMapping("/create-exercise")
    @PreAuthorize("hasAnyAuthority(" + AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION + ")")
    public @ResponseBody
    JsonResult createExercise(@RequestBody @Valid ExerciseCreateViewModel exerciseCreateViewModel, BindingResult bindingResult) {
        String message = "";
        boolean isSuccess = true;
        long savedId = 0;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = (FieldError) bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        } else {
            try {
                //check current user has permision to create exercise
                boolean permision = this.courseService.checkPermissionModifyCourse(exerciseCreateViewModel.getCourseId());
                if (permision) {
                    savedId = this.exerciseService.create(exerciseCreateViewModel);
                    message = AppMessage.getMessageSuccess(AppMessage.CREATE, AppMessage.EXERCISE);
                } else {
                    throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                }
            } catch (DataIntegrityViolationException ex) {
                message = AppMessage.getMessageFail(AppMessage.CREATE, AppMessage.EXERCISE);
                isSuccess = false;
            }
        }
        CreateResponse createResponse = new CreateResponse();
        createResponse.setSuccess(isSuccess);
        createResponse.setSavedId(savedId);
        return new JsonResult(message, createResponse);
    }

    @ApiOperation(value = "get exercise detail by id")
    @GetMapping("/get-by-id")
    @PreAuthorize("hasAnyAuthority(" + AppRole.ROLE_LEARNER_AUTHENTICATIION + ","
            + AppRole.ROLE_ADMIN_AUTHENTICATIION+","
            + AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION + ")")
    public @ResponseBody
    JsonResult getExerciseById(@RequestParam("exerciseId") long exerciseId) {
        long courseId = this.exerciseService.getCourseIdByExerciseId(exerciseId);
        UserPrincipal userPrincipal = this.userService.getCurrentUser();

        if(courseId == 0){
            throw new ResourceNotFoundException("Excercise","id",exerciseId);
        }
        if (!this.courseService.checkPermissionViewCourseDetail(courseId, userPrincipal)) {
            throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
        }
        Exercise exercise = this.exerciseService.getById(exerciseId).get();
        return new JsonResult("", exercise);
    }

    @ApiOperation(value = "get exercises by course id")
    @GetMapping("/get-by-course-id")
    @PreAuthorize("hasAnyAuthority(" + AppRole.ROLE_LEARNER_AUTHENTICATIION + ","
            + AppRole.ROLE_ADMIN_AUTHENTICATIION+","
            + AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION + ")")
    public @ResponseBody
    JsonResult getExerciseByCourseId(@RequestParam("courseId") long courseId) {
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        if(this.courseService.isExist(courseId)){
            throw new ResourceNotFoundException("Course","id",courseId);
        }

        if (!this.courseService.checkPermissionViewCourseDetail(courseId, userPrincipal)) {
            throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
        }
        return new JsonResult("", this.exerciseService.getByCourseId(courseId));
    }

    @ApiOperation(value = "Update exercise")
    @PutMapping("/update-exercise")
    @PreAuthorize("hasAnyAuthority(" + AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION + ")")
    public @ResponseBody
    JsonResult updateExercise(@RequestBody @Valid ExerciseUpdateViewModel exerciseCreateViewModel, BindingResult bindingResult) {
        String message = "";
        boolean isSuccess = true;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = (FieldError) bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        } else {
            try {
                Exercise exercise = this.exerciseService.getById(exerciseCreateViewModel.getExerciseId())
                        .orElseThrow(() -> new ResourceNotFoundException("Exercise", "id", exerciseCreateViewModel.getExerciseId()));
                //check current user has permision to create exercise
                boolean permision = this.courseService.checkPermissionModifyCourse(exercise.getCourse().getCourseId());
                if (permision) {
                    exercise.setQuestion(exerciseCreateViewModel.getQuestion());
                    exercise.setAnswer(exerciseCreateViewModel.getAnswer());
                    this.exerciseService.save(exercise);
                    message = AppMessage.getMessageSuccess(AppMessage.UPDATE, AppMessage.EXERCISE);
                } else {
                    throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                }
            } catch (DataIntegrityViolationException ex) {
                message = AppMessage.getMessageFail(AppMessage.UPDATE, AppMessage.EXERCISE);
                isSuccess = false;
            }
        }
        return new JsonResult(message, isSuccess);
    }

    @ApiOperation(value = "Remove exercise")
    @PutMapping("/remove-exercise")
    @PreAuthorize("hasAnyAuthority(" + AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION + ")")
    public @ResponseBody
    JsonResult removeExercise(@RequestBody @Valid ExerciseRemoveViewModel exerciseRemoveViewModel, BindingResult bindingResult) {
        String message = "";
        boolean isSuccess = true;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = (FieldError) bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        } else {
            try {
                Exercise exercise = this.exerciseService.getById(exerciseRemoveViewModel.getExerciseId())
                        .orElseThrow(() -> new ResourceNotFoundException("Exercise", "id", exerciseRemoveViewModel.getExerciseId()));
                //check current user has permision to create exercise
                boolean permision = this.courseService.checkPermissionModifyCourse(exercise.getCourse().getCourseId());
                if (permision) {
                    this.exerciseService.deleteById(exercise.getExerciseId());
                    message = AppMessage.getMessageSuccess(AppMessage.UPDATE, AppMessage.EXERCISE);
                } else {
                    throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                }
            } catch (DataIntegrityViolationException ex) {
                message = AppMessage.getMessageFail(AppMessage.UPDATE, AppMessage.EXERCISE);
                isSuccess = false;
            }
        }
        return new JsonResult(message, isSuccess);
    }
}
