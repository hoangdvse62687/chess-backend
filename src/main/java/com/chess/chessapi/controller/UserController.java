package com.chess.chessapi.controller;


import com.chess.chessapi.entities.Cetificates;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.exception.ResourceNotFoundException;
import com.chess.chessapi.model.JsonResult;
import com.chess.chessapi.security.CurrentUser;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.CetificatesService;
import com.chess.chessapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
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
}
