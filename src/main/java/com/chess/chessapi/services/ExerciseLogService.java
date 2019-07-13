package com.chess.chessapi.services;

import com.chess.chessapi.entities.Exercise;
import com.chess.chessapi.entities.ExerciseLog;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.repositories.ExerciseLogRepository;
import com.chess.chessapi.utils.TimeUtils;
import com.chess.chessapi.viewmodels.ExerciseLogCreateViewModel;
import com.chess.chessapi.viewmodels.ExerciseLogUpdateViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExerciseLogService {
    @Autowired
    private ExerciseLogRepository exerciseLogRepository;

    @PersistenceContext
    private EntityManager em;

    public List<Long> getAllExerciseIdsInLogByCourseIdAndUserId(long userId,long courseId){
        StoredProcedureQuery storedProcedureQuery = this.em.createNamedStoredProcedureQuery("getLogExerciseidByUseridAndCourseid");
        storedProcedureQuery.setParameter("userId",userId);
        storedProcedureQuery.setParameter("courseId",courseId);

        storedProcedureQuery.execute();

        List<Long> data = new ArrayList<>();
        for (Object item :
                storedProcedureQuery.getResultList()) {
            data.add(Long.parseLong(item.toString()));
        }
        return data;
    }

    public Long create(ExerciseLogCreateViewModel exerciseLogCreateViewModel,long userId){
        ExerciseLog exerciseLog = new ExerciseLog();
        exerciseLog.setCreatedDate(TimeUtils.getCurrentTime());
        exerciseLog.setPassed(exerciseLogCreateViewModel.isPassed());
        Exercise exercise = new Exercise();
        exercise.setExerciseId(exerciseLogCreateViewModel.getExerciseId());
        exerciseLog.setExercise(exercise);
        User user = new User();
        user.setUserId(userId);
        exerciseLog.setUser(user);

        ExerciseLog savedExcerciseLog = this.exerciseLogRepository.save(exerciseLog);
        return savedExcerciseLog.getExerciseLogId();
    }

    public void update(ExerciseLogUpdateViewModel exerciseLogUpdateViewModel){
        this.exerciseLogRepository.update(exerciseLogUpdateViewModel.getExerciseLogId(),exerciseLogUpdateViewModel.isPassed());
    }

    public long getUserIdById(long id){
        return this.exerciseLogRepository.findUserIdById(id);
    }
}
