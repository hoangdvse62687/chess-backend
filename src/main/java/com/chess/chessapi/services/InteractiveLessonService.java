package com.chess.chessapi.services;

import com.chess.chessapi.entities.InteractiveLesson;
import com.chess.chessapi.repositories.InteractiveLessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InteractiveLessonService {
    @Autowired
    private InteractiveLessonRepository interactiveLessonRepository;

    //public method

    public InteractiveLesson create(InteractiveLesson interactiveLesson){
        return this.interactiveLessonRepository.save(interactiveLesson);
    }

    public void update(long iLessonId,String initCode,String content){
        this.interactiveLessonRepository.update(iLessonId,initCode,content);
    }

    public void delete(long iLessonId){
        this.interactiveLessonRepository.deleteById(iLessonId);
    }
    //end public method
}
