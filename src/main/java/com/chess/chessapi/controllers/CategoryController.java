package com.chess.chessapi.controllers;

import com.chess.chessapi.entities.Category;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.services.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/course")
@Api(value = "Course Management")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "get categories")
    @GetMapping("/get-categories")
    public JsonResult getCategories(){
        return new JsonResult(null,this.categoryService.getAllCategory());
    }

    @ApiOperation(value = "get category detail by id")
    @GetMapping("/get-category-detail-by-id")
    public JsonResult getCategoryById(long categoryId){
        Category category = this.categoryService.getCategoryById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","id",categoryId));
        this.categoryService.getCategoryDetails(category);
        return new JsonResult(null,category);
    }
}
