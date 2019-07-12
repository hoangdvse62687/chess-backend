package com.chess.chessapi.services;

import com.chess.chessapi.entities.UninteractiveLesson;
import com.chess.chessapi.repositories.UninteractiveLessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UninteractiveLessonService {
    @Autowired
    private UninteractiveLessonRepository uninteractiveLessonRepository;

    public void update(long uiLessonId,String content){
        this.uninteractiveLessonRepository.update(uiLessonId,content);
    }

    public UninteractiveLesson create(UninteractiveLesson uninteractiveLesson){
        return this.uninteractiveLessonRepository.save(uninteractiveLesson);
    }
}
