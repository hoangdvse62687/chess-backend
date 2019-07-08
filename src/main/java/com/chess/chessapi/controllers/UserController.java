package com.chess.chessapi.controllers;


import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.constants.EntitiesFieldName;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.exceptions.AccessDeniedException;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.security.CurrentUser;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.UserService;
import com.chess.chessapi.viewmodels.UserPaginationViewModel;
import com.chess.chessapi.viewmodels.UserUpdateStatusViewModel;
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
@RequestMapping(value = "/user")
@Api(value = "User Management")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "Get an current user detail")
    @GetMapping(value = "/get-current-user-detail")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody JsonResult getCurrentUserDetail(@CurrentUser UserPrincipal userPrincipal) {
        User user = this.userService.getUserById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User","id",userPrincipal.getId()));
        this.userService.getUserDetails(user);
        return new JsonResult("",user);
    }

    @ApiOperation(value = "Get an current user ")
    @GetMapping(value = "/get-current-user")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody JsonResult getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return new JsonResult("",userPrincipal);
    }

    @ApiOperation(value = "Register an user")
    @PutMapping(value = "/register")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_REGISTRATION_AUTHENTICATIION +")")
    public @ResponseBody JsonResult register(@Valid @RequestBody User user,BindingResult bindingResult){

        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            try{
                //gain redirect uri base on role
                this.userService.register(user);
                message = AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.USER);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.USER);
                isSuccess = false;
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "Update profile user ")
    @PutMapping(value = "/update-profile")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody JsonResult updateProfile(@Valid @RequestBody User user, BindingResult bindingResult){
        if(!this.userService.checkPermissionModify(user.getUserId())){
            throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
        }

        if(!this.userService.isExist(user.getUserId())){
            new ResourceNotFoundException("User","id",user.getUserId());
        }

        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            try{
                this.userService.updateProfile(user);

                message =  AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.PROFILE);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.PROFILE);
                isSuccess = false;
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "Get user pagination")
    @GetMapping(value = "/get-users-pagination")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_ADMIN_AUTHENTICATIION+")")
    public @ResponseBody JsonResult getUsers(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize
            ,String email, String role, String isActive){

        if(email == null){
            email = "";
        }
        if(role == null){
            role = "";
        }
        if(isActive == null){
            isActive = "";
        }
        email = '%' + email + '%';

        PagedList<UserPaginationViewModel> data = null;
        try{
            data = this.userService.getPagination(page,pageSize,email,role,isActive);
        }catch (IllegalArgumentException ex){
            throw new ResourceNotFoundException("Page","number",page);
        }

        return new JsonResult(null,data);
    }

    @ApiOperation(value = "Get an user by id")
    @GetMapping(value = "/get-by-id")
    public @ResponseBody JsonResult getUserById(@RequestParam("userId") long userId){
        User user = this.userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User","id",userId));
        this.userService.getUserDetails(user);
        return new JsonResult(null,user);
    }

    @ApiOperation(value = "Update user status")
    @PutMapping(value = "/update-status")
    @PreAuthorize("hasAuthority("+AppRole.ROLE_ADMIN_AUTHENTICATIION+")")
    public @ResponseBody JsonResult updateStatus(@RequestBody UserUpdateStatusViewModel userUpdateStatusViewModel){
        Boolean isSuccess = true;
        String message = "";
        try{
            User user = this.userService.getUserById(userUpdateStatusViewModel.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User","id",userUpdateStatusViewModel.getUserId()));
            this.userService.updateStatus(user,userUpdateStatusViewModel.getUserId(),userUpdateStatusViewModel.isActive());
            message = AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.USER);
        }catch (DataIntegrityViolationException ex){
            isSuccess = false;
            message =  AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.USER);
        }
        return new JsonResult(message,isSuccess);
    }
}
