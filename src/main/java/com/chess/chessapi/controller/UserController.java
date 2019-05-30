package com.chess.chessapi.controller;


import com.chess.chessapi.constant.AppMessage;
import com.chess.chessapi.constant.EntitiesFieldName;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.exception.ResourceNotFoundException;
import com.chess.chessapi.model.JsonResult;
import com.chess.chessapi.model.PagedList;
import com.chess.chessapi.model.PaginationCustom;
import com.chess.chessapi.security.CurrentUser;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.UserService;
import com.chess.chessapi.viewmodel.UserPagination;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
        User user = userService.getUserById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User","id",userPrincipal.getId()));
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
    @PreAuthorize("hasAuthority('ROLE_REGISTRATION')")
    public @ResponseBody JsonResult register(@Valid @RequestBody User user,@RequestParam("redirectUri") String redirectUri
            , BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            try{
                //gain redirect uri base on role
                message = userService.register(user,redirectUri);

            }catch (Exception ex){
                message = ex.getMessage();
                isSuccess = false;
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "Update profile user ")
    @PutMapping(value = "/update-profile")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody JsonResult updateProfile(@Valid @RequestBody User user, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            try{
                userService.updateProfile(user);

                message =  AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.PROFILE);;
            }catch (Exception ex){
                message = AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.PROFILE);
                isSuccess = false;
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "Get user pagings")
    @GetMapping(value = "/get-users-pagination")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public @ResponseBody JsonResult getUsers(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize
            ,String email,String roleSort,boolean sortFullName,String sortStatus){
        if(fullName == null){
            fullName = '%' + "" + '%';
        }else{
            fullName = '%' + email + '%';
        }
        PaginationCustom<UserPagination> userPage = null;
        try{
            userPage = userService.getPagination(page,pageSize,email,roleSort,sortFullName,sortStatus);
        }catch (IllegalArgumentException ex){
            throw new ResourceNotFoundException("Page","number",page);
        }
        PagedList<UserPagination> data = new PagedList<>(userPage.getTotalPages(),userPage.getTotalElements(),userPage.getContent());

        return new JsonResult(null,data);
    }

    @ApiOperation(value = "Get an user by id")
    @GetMapping(value = "/get-by-id")
    public @ResponseBody JsonResult getUserById(@RequestParam("userId") long userId){
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User","id",userId));
        return new JsonResult(null,user);
    }

    @ApiOperation(value = "Update user status")
    @PutMapping(value = "/update-status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public @ResponseBody JsonResult updateStatus(@RequestParam("userId") int userId,@RequestParam("isActive") int isActive ){
        Boolean isSuccess = true;
        String message = "";
        try{
            userService.updateStatus(userId,isActive);
            message = AppMessage.getMessageSuccess(AppMessage.UPDATE,AppMessage.USER, EntitiesFieldName.USER_ID,Integer.toString(userId));
        }catch (Exception ex){
            isSuccess = false;
            message =  AppMessage.getMessageFail(AppMessage.UPDATE,AppMessage.USER, EntitiesFieldName.USER_ID,Integer.toString(userId));
        }
        return new JsonResult(message,isSuccess);
    }
}
