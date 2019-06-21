package com.chess.chessapi.services;

import com.chess.chessapi.repositories.LearningLogRepository;
import com.chess.chessapi.viewmodels.LearningLogCreateViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LearningLogService {
    @Autowired
    private LearningLogRepository learningLogRepository;

    public void create(LearningLogCreateViewModel learningLogCreateViewModel,long userId){
        this.learningLogRepository.create(userId,learningLogCreateViewModel.getCourseId(),
                learningLogCreateViewModel.getLessonId(),learningLogCreateViewModel.getFinishedDate());
    }

    public List<Long> getAllByCourseId(long courseId,long userId){
        return this.learningLogRepository.findAllByCourseIdAndUserId(courseId,userId);
    }
}
