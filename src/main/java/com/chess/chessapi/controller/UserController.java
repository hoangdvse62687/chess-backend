package com.chess.chessapi.controller;


import com.chess.chessapi.constant.AppMessage;
import com.chess.chessapi.entities.Cetificates;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.exception.ResourceNotFoundException;
import com.chess.chessapi.model.JsonResult;
import com.chess.chessapi.model.PagedList;
import com.chess.chessapi.security.CurrentUser;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.CetificatesService;
import com.chess.chessapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CetificatesService cetificatesService;

    @GetMapping(value = "/user/getCurrentUserDetail")
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody JsonResult getCurrentUserDetail(@CurrentUser UserPrincipal userPrincipal) {
        User user = userService.getUserById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User","id",userPrincipal.getId()));
        return new JsonResult("",user);
    }

    @PutMapping(value = "/user/register")
    @PreAuthorize("hasAuthority('ROLE_REGISTRATION')")
    public @ResponseBody JsonResult register(@Valid @RequestBody User user, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            try{
                //gain redirect uri base on role
                message = userService.register(user);
                for (Cetificates cetificates:
                        user.getCetificates()) {
                    cetificates.setUser(user);
                    cetificatesService.create(cetificates);
                }
            }catch (Exception ex){
                message = ex.getMessage();
                isSuccess = false;
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @GetMapping(value = "/user/loadAll")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public @ResponseBody JsonResult loadAll(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize
            ,String fullName,String roleSort,boolean sortFullName){
        if(fullName == null){
            fullName = '%' + "" + '%';
        }else{
            fullName = '%' + fullName + '%';
        }
        Page<User> userPage = null;
        try{
            userPage = userService.getAllByFullName(page,pageSize,fullName,roleSort,sortFullName);
        }catch (IllegalArgumentException ex){
            throw new ResourceNotFoundException("Page","number",page);
        }
        PagedList<User> data = new PagedList<>(userPage.getTotalPages(),userPage.getTotalElements(),userPage.getContent());
        return new JsonResult(null,data);
    }

    @GetMapping(value = "user/getUserById")
    public @ResponseBody JsonResult getUserById(@RequestParam("userId") long userId){
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User","id",userId));
        return new JsonResult(null,user);
    }

    @GetMapping(value = "/user/updateStatus")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public @ResponseBody JsonResult updateStatus(@RequestParam("userId") int userId,@RequestParam("isActive") int isActive ){
        Boolean isSuccess = true;
        String message = "";
        try{
            userService.updateStatus(userId,isActive);
            message = AppMessage.UPDATE_USER_STATUS_SUCCESSFUL + userId;
        }catch (Exception ex){
            isSuccess = false;
            message = AppMessage.UPDATE_USER_STATUS_FAIL + userId;
        }
        return new JsonResult(message,isSuccess);
    }
}
