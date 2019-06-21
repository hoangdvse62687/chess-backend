package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.entities.Category;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.services.CategoryService;
import com.chess.chessapi.viewmodels.CategoryViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/category")
@Api(value = "Category Management")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "get categories")
    @GetMapping("/get-categories")
    public @ResponseBody JsonResult getCategories(){
        return new JsonResult(null,this.categoryService.getAllCategory());
    }

    @ApiOperation(value = "get category detail by id")
    @GetMapping("/get-by-id")
    public @ResponseBody JsonResult getCategoryById(@RequestParam("categoryId") long categoryId){
        Category category = this.categoryService.getCategoryById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","id",categoryId));
        this.categoryService.getCategoryDetails(category);
        return new JsonResult(null,category);
    }

    @ApiOperation(value = "remove category")
    @PutMapping("/remove-category")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_ADMIN_AUTHENTICATIION+")")
    public @ResponseBody JsonResult removeCategoryById(@RequestBody CategoryViewModel Category){
        Category category = this.categoryService.getCategoryById(Category.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category","id",Category.getCategoryId()));
        Boolean isSuccess = true;
        String message = "";
        try{
            this.categoryService.removeCategory(category);
            message = AppMessage.getMessageSuccess(AppMessage.DELETE,AppMessage.CATEGORY);
        }catch (Exception ex){
            isSuccess = false;
            message =  AppMessage.getMessageFail(AppMessage.DELETE,AppMessage.CATEGORY);
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "create category")
    @PostMapping("/create-category")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_ADMIN_AUTHENTICATIION+")")
    public @ResponseBody JsonResult createCategory(@RequestBody @Valid CategoryViewModel category, BindingResult bindingResult){
        Boolean isSuccess = true;
        String message = "";
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else {
            try {
                this.categoryService.create(category);
                message = AppMessage.getMessageSuccess(AppMessage.CREATE, AppMessage.CATEGORY);
            } catch (Exception ex) {
                isSuccess = false;
                message = AppMessage.getMessageFail(AppMessage.CREATE, AppMessage.CATEGORY);
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "update category")
    @PostMapping("/update-category")
    @PreAuthorize("hasAuthority("+ AppRole.ROLE_ADMIN_AUTHENTICATIION+")")
    public @ResponseBody JsonResult updateCategory(@RequestBody @Valid CategoryViewModel category, BindingResult bindingResult){
        Boolean isSuccess = true;
        String message = "";
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else {
            try {
                this.categoryService.update(category);
                message = AppMessage.getMessageSuccess(AppMessage.UPDATE, AppMessage.CATEGORY);
            } catch (Exception ex) {
                isSuccess = false;
                message = AppMessage.getMessageFail(AppMessage.UPDATE, AppMessage.CATEGORY);
            }
        }
        return new JsonResult(message,isSuccess);
    }
}
