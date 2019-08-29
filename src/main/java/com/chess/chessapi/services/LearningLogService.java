package com.chess.chessapi.services;

import com.chess.chessapi.entities.Course;
import com.chess.chessapi.entities.LearningLog;
import com.chess.chessapi.entities.Lesson;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.repositories.LearningLogRepository;
import com.chess.chessapi.utils.ManualCastUtils;
import com.chess.chessapi.utils.TimeUtils;
import com.chess.chessapi.viewmodels.LearningLogCreateViewModel;
import com.chess.chessapi.viewmodels.LearningLogViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LearningLogService {
    @Autowired
    private LearningLogRepository learningLogRepository;

    public long create(LearningLogCreateViewModel learningLogCreateViewModel,long userId){
        LearningLog learningLog = new LearningLog();
        Course course = new Course();
        course.setCourseId(learningLogCreateViewModel.getCourseId());
        learningLog.setCourse(course);
        learningLog.setFinishedDate(TimeUtils.getCurrentTime());
        Lesson lesson = new Lesson();
        lesson.setLessonId(learningLogCreateViewModel.getLessonId());
        learningLog.setLesson(lesson);
        learningLog.setPassed(learningLog.isPassed());
        User user = new User();
        user.setUserId(userId);
        learningLog.setUser(user);

        LearningLog savedLearningLog = this.learningLogRepository.save(learningLog);
        return savedLearningLog.getLearninglogId();
    }

    public void update(LearningLog learningLog,boolean isPassed){
        learningLog.setPassed(isPassed);
        this.learningLogRepository.save(learningLog);
    }

    public List<LearningLogViewModel> getAllByCourseId(long courseId, long userId){
        return ManualCastUtils.castListObjectsToLearningLogViewModel(this.learningLogRepository.findAllByCourseIdAndUserId(courseId,userId));
    }

    public void deleteAllByLessonId(long lessonId){
        this.learningLogRepository.deleteAllByLessonId(lessonId);
    }

    public void deleteAllByLessonIdAndCourseId(long lessonId,long courseId){
        this.learningLogRepository.deleteAllByLessonIdAndCourseId(lessonId,courseId);
    }

    public Optional<LearningLog> getById(long learningLogId){
        return this.learningLogRepository.findById(learningLogId);
    }
}
