package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.entities.GameHistory;
import com.chess.chessapi.exceptions.AccessDeniedException;
import com.chess.chessapi.exceptions.ResourceNotFoundException;
import com.chess.chessapi.models.CreateResponse;
import com.chess.chessapi.models.JsonResult;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.GameHistoryService;
import com.chess.chessapi.services.UserService;
import com.chess.chessapi.viewmodels.GameHistoryCreateViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/game-history")
@Api(value = "Exercise Management")
public class GameHistoryController {
    @Autowired
    private GameHistoryService gameHistoryService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Create game history")
    @PostMapping("/create-game-history")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody JsonResult createGameHistory(@RequestBody @Valid GameHistoryCreateViewModel gameHistoryCreateViewModel, BindingResult bindingResult){
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
                if(gameHistoryCreateViewModel.getPoint() < 0){
                    //check point has enough to decrease
                    float userPoint = this.userService.getPointByUserId(userPrincipal.getId());
                    if(userPoint < -gameHistoryCreateViewModel.getPoint()){
                        throw new AccessDeniedException(AppMessage.POINT_DENY_MESSAGE);
                    }
                }
                savedId = this.gameHistoryService.create(gameHistoryCreateViewModel,userPrincipal.getId());
                message = AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.GAME_HISTORY);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.GAME_HISTORY);
                isSuccess = false;
            }
        }
        CreateResponse createResponse = new CreateResponse();
        createResponse.setSuccess(isSuccess);
        createResponse.setSavedId(savedId);
        return new JsonResult(message,createResponse);
    }

    @ApiOperation(value = "Get game history paginations")
    @GetMapping("/get-game-history-paginations")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody JsonResult getGameHistory(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize){
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        Page<GameHistory> listGameHistory = null;
        try {
            listGameHistory = this.gameHistoryService.getPagination(page,pageSize,userPrincipal.getId());
        }catch (IllegalArgumentException ex){
            throw new ResourceNotFoundException("Page","number",page);
        }
        PagedList<GameHistory> data = new PagedList<GameHistory>(listGameHistory.getTotalPages()
                ,listGameHistory.getTotalElements(),listGameHistory.getContent());
        return new JsonResult(null,data);
    }
}
