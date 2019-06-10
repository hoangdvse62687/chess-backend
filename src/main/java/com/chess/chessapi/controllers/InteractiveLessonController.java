package com.chess.chessapi.controllers;

import com.chess.chessapi.entities.InteractiveLesson;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.services.InteractiveLessonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/interactive-lesson")
@Api(value = "Interactive Lesson Management")
public class InteractiveLessonController {
    @Autowired
    private InteractiveLessonService interactiveLessonService;

    @ApiOperation(value = "Get an interactive lesson by id")
    @GetMapping(value = "/get-interactive-lesson-by-id")
    public @ResponseBody JsonResult getInteractiveLessonById(@RequestParam("interactiveLessonId") long interactiveLessonId){
        InteractiveLesson interactiveLesson = this.interactiveLessonService.getInteractiveLessonById(interactiveLessonId)
                .orElseThrow(() -> new ResourceNotFoundException("InteractiveLesson","id",interactiveLessonId));
        return new JsonResult(null,interactiveLesson);
    }
}
