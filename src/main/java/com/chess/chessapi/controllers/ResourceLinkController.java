package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.CreateResponse;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.ResourceLinkService;
import com.chess.chessapi.services.UserService;
import com.chess.chessapi.viewmodels.ResourceLinkCreateViewModel;
import com.chess.chessapi.viewmodels.ResourceLinkRemoveViewModel;
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
@RequestMapping(value = "/resource-link")
@Api(value = "Resource Link Management")
public class ResourceLinkController {
    @Autowired
    private ResourceLinkService resourceLinkService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Create resource link")
    @PostMapping("/create-resource-link")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult createResourceLink(@Valid @RequestBody ResourceLinkCreateViewModel resourceLinkCreateViewModel
            , BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        long savedId = 0;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else {
            try{
                UserPrincipal userPrincipal = this.userService.getCurrentUser();
                savedId = this.resourceLinkService.create(resourceLinkCreateViewModel.getLink(),userPrincipal.getId());
                message =  AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.RESOURCE_LINK);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.RESOURCE_LINK);
                isSuccess = false;
            }
        }
        CreateResponse createResponse = new CreateResponse();
        createResponse.setSuccess(isSuccess);
        createResponse.setSavedId(savedId);
        return new JsonResult(message,createResponse);
    }

    @ApiOperation(value = "Remove resource link")
    @PutMapping("/remove-resource-link")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult removeResourceLink(@Valid @RequestBody ResourceLinkRemoveViewModel resourceLinkRemoveViewModel
            , BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else {
            try{
                if(!this.resourceLinkService.isExist(resourceLinkRemoveViewModel.getResourcelinkId())){
                    throw new ResourceNotFoundException("Resource link","id",resourceLinkRemoveViewModel.getResourcelinkId());
                }
                this.resourceLinkService.remove(resourceLinkRemoveViewModel.getResourcelinkId());
                message =  AppMessage.getMessageSuccess(AppMessage.DELETE,AppMessage.RESOURCE_LINK);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.DELETE,AppMessage.RESOURCE_LINK);
                isSuccess = false;
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "get resource link of current user")
    @GetMapping("/get-resource-link")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_INSTRUCTOR_AUTHENTICATIION+")")
    public @ResponseBody JsonResult getResourceLinkCurrentUser(){
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        return new JsonResult(null,this.resourceLinkService.getAllLinkByUserId(userPrincipal.getId()));
    }
}
