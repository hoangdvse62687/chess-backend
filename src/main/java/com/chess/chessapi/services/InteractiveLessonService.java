package com.chess.chessapi.services;

import com.chess.chessapi.entities.InteractiveLesson;
import com.chess.chessapi.entities.Step;
import com.chess.chessapi.repositories.InteractiveLessonRepository;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.utils.TimeUtils;
import com.chess.chessapi.viewmodels.CourseDetailViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
public class InteractiveLessonService {
    @Autowired
    private InteractiveLessonRepository interactiveLessonRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StepService stepService;

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

    public void update(long iLessonId,String initCode){
        this.interactiveLessonRepository.update(iLessonId,initCode);
    }

    public void delete(long iLessonId){
        this.interactiveLessonRepository.deleteById(iLessonId);
    }
    //end public method
}
