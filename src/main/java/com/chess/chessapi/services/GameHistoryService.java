package com.chess.chessapi.services;

import com.chess.chessapi.constants.EntitiesFieldName;
import com.chess.chessapi.constants.GameHistoryStatus;
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

    @Autowired
    private PointLogService pointLogService;

    private final String BET_MESSAGE = "Bạn đã đặt cược ";
    private final String DRAWN_MESSAGE = "Bạn đã hòa máy cấp độ ";
    private final String LOSE_MESSAGE = "Bạn đã thua máy cấp độ ";
    private final String WIN_MESSAGE = "Bạn đã thắng máy cấp độ ";
    private final String POINT = " điểm ";
    private final String WHEN_PLAYING_BOT = "khi chơi cờ với máy cấp độ ";
    private final String RECEIVE = ". Và nhận được ";

    //PUBLIC METHOD DEFINED
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public long create(GameHistoryCreateViewModel gameHistoryCreateViewModel,long userId){
        GameHistory gameHistory = new GameHistory();
        gameHistory.setColor(gameHistory.getColor());
        gameHistory.setGameTime(gameHistoryCreateViewModel.getGameTime());
        gameHistory.setLevel(gameHistoryCreateViewModel.getLevel());
        gameHistory.setPoint(gameHistoryCreateViewModel.getPoint());
        gameHistory.setRecord(gameHistoryCreateViewModel.getRecord());
        gameHistory.setStartTime(gameHistoryCreateViewModel.getStartTime());
        gameHistory.setStatus(gameHistoryCreateViewModel.getStatus());
        User user = new User();
        user.setUserId(userId);
        gameHistory.setUser(user);

        this.handleDataCreateUpdate(userId,gameHistory);

        GameHistory savedGameHistory = this.gameHistoryRepository.save(gameHistory);
        return savedGameHistory.getGamehistoryId();
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void update(GameHistory gameHistory,long userId){
        User user = new User();
        user.setUserId(userId);
        gameHistory.setUser(user);
        this.handleDataCreateUpdate(userId,gameHistory);

        this.gameHistoryRepository.save(gameHistory);
    }
    public Page<GameHistory> getPagination(int page,int pageSize,long userId){
        PageRequest pageable = PageRequest.of(page - 1,pageSize, Sort.by(EntitiesFieldName.GAME_HISTORY_START_TIME).descending());
        return this.gameHistoryRepository.findAllByUserId(pageable,userId);
    }

    public boolean checkPointBet(long userId,int status,float pointRequired){
        boolean result = true;
        float userPoint = this.userService.getPointByUserId(userId);
        if(status == GameHistoryStatus.BET){
            if(userPoint < pointRequired){
                return false;
            }
        }
        return result;
    }
    //END PUBLIC METHOD DEFINED

    //PRIVATE METHOD DEFINED
    private String getContentPointLog(int status,float point,int level) {
        String content = "";
        point = point < 0 ? -point : point;
        String pointStr = Integer.toString((int)point);
        switch (status) {
            case GameHistoryStatus.BET:
                content = BET_MESSAGE + pointStr + POINT + WHEN_PLAYING_BOT + level;
                break;
            case GameHistoryStatus.DRAWN:
                content = DRAWN_MESSAGE + level + RECEIVE + pointStr + POINT;
                break;
            case GameHistoryStatus.LOSE:
                content = LOSE_MESSAGE + level + RECEIVE + pointStr + POINT;
                break;
            case GameHistoryStatus.WIN:
                content = WIN_MESSAGE + level + RECEIVE + pointStr + POINT;
                break;

        }
        return content;
    }

    private void handleDataCreateUpdate(long userId, GameHistory gameHistory){
        //update point user
        float pointUpdate = gameHistory.getStatus() == GameHistoryStatus.BET ? -gameHistory.getPoint()
                : gameHistory.getPoint();

        this.userService.increasePoint(userId,pointUpdate);

        //write point log
        String content = this.getContentPointLog(gameHistory.getStatus(),
                gameHistory.getPoint(),gameHistory.getLevel());
        this.pointLogService.create(content,gameHistory.getPoint(),
                userId);
    }
    //END PRIVATE METHOD DEFINED
}
