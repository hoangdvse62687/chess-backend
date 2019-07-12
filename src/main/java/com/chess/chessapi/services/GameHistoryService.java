package com.chess.chessapi.services;

import com.chess.chessapi.constants.EntitiesFieldName;
import com.chess.chessapi.entities.GameHistory;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.repositories.GameHistoryRepository;
import com.chess.chessapi.viewmodels.GameHistoryCreateViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameHistoryService {
    @Autowired
    private GameHistoryRepository gameHistoryRepository;

    @Autowired
    private UserService userService;

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public long create(GameHistoryCreateViewModel gameHistoryCreateViewModel,long userId){
        GameHistory gameHistory = new GameHistory();
        gameHistory.setColor(gameHistory.getColor());
        gameHistory.setGameTime(gameHistoryCreateViewModel.getGameTime());
        gameHistory.setLevel(gameHistoryCreateViewModel.getLevel());
        gameHistory.setPoint(gameHistoryCreateViewModel.getPoint());
        gameHistory.setRecord(gameHistoryCreateViewModel.getRecord());
        gameHistory.setStartTime(gameHistoryCreateViewModel.getStartTime());
        User user = new User();
        user.setUserId(userId);
        gameHistory.setUser(user);

        //update point user
        this.userService.increasePoint(userId,gameHistoryCreateViewModel.getPoint());

        GameHistory savedGameHistory = this.gameHistoryRepository.save(gameHistory);
        return savedGameHistory.getGamehistoryId();
    }

    public Page<GameHistory> getPagination(int page,int pageSize,long userId){
        PageRequest pageable = PageRequest.of(page - 1,pageSize, Sort.by(EntitiesFieldName.GAME_HISTORY_START_TIME).descending());
        return this.gameHistoryRepository.findAllByUserId(pageable,userId);
    }
}
