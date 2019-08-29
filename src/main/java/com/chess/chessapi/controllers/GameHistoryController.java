package com.chess.chessapi.controllers;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.constants.GameHistoryStatus;
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
import com.chess.chessapi.viewmodels.GameHistoryUpdateViewModel;
import com.chess.chessapi.viewmodels.GameHistoryViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@Api(value = "Exercise Management")
public class GameHistoryController {
    @Autowired
    private GameHistoryService gameHistoryService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Create game history")
    @PostMapping("/game-history")
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
                if(!this.gameHistoryService.checkPointBet(userPrincipal.getId(),GameHistoryStatus.BET
                        ,gameHistoryCreateViewModel.getPoint())){
                    throw new AccessDeniedException(AppMessage.POINT_DENY_MESSAGE);
                }
                savedId = this.gameHistoryService.create(gameHistoryCreateViewModel,userPrincipal.getId());
                message = AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.GAME_HISTORY);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.GAME_HISTORY);
                isSuccess = false;
                Logger.getLogger(GameHistoryController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        CreateResponse createResponse = new CreateResponse();
        createResponse.setSuccess(isSuccess);
        createResponse.setSavedId(savedId);
        return new JsonResult(message,createResponse);
    }

    @ApiOperation(value = "Update game history")
    @PutMapping("/game-history")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody JsonResult updateGameHistory(@RequestBody @Valid GameHistoryUpdateViewModel gameHistoryUpdateViewModel, BindingResult bindingResult){
        String message = "";
        boolean isSuccess = true;
        if(bindingResult.hasErrors()){
            FieldError fieldError = (FieldError)bindingResult.getAllErrors().get(0);
            message = fieldError.getDefaultMessage();
            isSuccess = false;
        }else{
            try{
                GameHistory gameHistory = this.gameHistoryService.getById(gameHistoryUpdateViewModel.getGameHistoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Game History","id",gameHistoryUpdateViewModel.getGameHistoryId()));
                UserPrincipal userPrincipal = this.userService.getCurrentUser();
                if(!this.gameHistoryService.checkPointBet(userPrincipal.getId(),gameHistoryUpdateViewModel.getStatus()
                        ,gameHistoryUpdateViewModel.getPoint())){
                    throw new AccessDeniedException(AppMessage.POINT_DENY_MESSAGE);
                }
                gameHistoryService.update(gameHistory,gameHistoryUpdateViewModel,userPrincipal.getId());
                message = AppMessage.getMessageSuccess(AppMessage.CREATE,AppMessage.GAME_HISTORY);
            }catch (DataIntegrityViolationException ex){
                message = AppMessage.getMessageFail(AppMessage.CREATE,AppMessage.GAME_HISTORY);
                isSuccess = false;
                Logger.getLogger(GameHistoryController.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        return new JsonResult(message,isSuccess);
    }

    @ApiOperation(value = "Get current user game history paginations")
    @GetMapping("/current-user")
    @PreAuthorize("hasAnyAuthority("+ AppRole.ROLE_LEARNER_AUTHENTICATIION+")")
    public @ResponseBody JsonResult getGameHistory(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize){
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        PagedList<GameHistoryViewModel> data = null;
        try {
            data = this.gameHistoryService.getPagination(page,pageSize,userPrincipal.getId());
        }catch (IllegalArgumentException ex){
            Logger.getLogger(GameHistoryController.class.getName()).log(Level.SEVERE,null,ex);
            throw new ResourceNotFoundException("Page","number",page);
        }
        return new JsonResult(null,data);
    }
}
