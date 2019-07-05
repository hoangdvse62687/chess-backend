package com.chess.chessapi.services;

import com.chess.chessapi.entities.InteractiveLesson;
import com.chess.chessapi.repositories.InteractiveLessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Service
public class InteractiveLessonService {
    @Autowired
    private InteractiveLessonRepository interactiveLessonRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @PersistenceContext
    private EntityManager em;

    //public method
    public Optional<InteractiveLesson> getInteractiveLessonById(long interactiveLessonId){
        return this.interactiveLessonRepository.findById(interactiveLessonId);
    }

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
