package com.chess.chessapi.services;

import com.chess.chessapi.entities.InteractiveLesson;
import com.chess.chessapi.repositories.InteractiveLessonRepository;
import com.chess.chessapi.utils.ManualCastUtils;
import com.chess.chessapi.viewmodels.CourseDetailViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;
import java.util.Optional;

@Service
public class InteractiveLessonService {
    @Autowired
    private InteractiveLessonRepository interactiveLessonRepository;

    @PersistenceContext
    private EntityManager em;

    public Optional<InteractiveLesson> getInteractiveLessonById(long interactiveLessonId){
        return this.interactiveLessonRepository.findById(interactiveLessonId);
    }

    public void getInteractiveLessonDetail(InteractiveLesson interactiveLesson){
        if(interactiveLesson != null){
            interactiveLesson.setCourseDetailViewModels(this.getCourseDetail(interactiveLesson.getInteractiveLessonId()));
        }
    }

    public List<CourseDetailViewModel> getCourseDetail(long interactiveLessonId){
        //getting courses by interactiveLessonId
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getCourseByInteractiveId");
        query.setParameter("interactiveId",interactiveLessonId);

        query.execute();
        return ManualCastUtils.castListObjectToCourseDetails(query.getResultList());
        //end getting courses by interactiveLessonId
    }
}
