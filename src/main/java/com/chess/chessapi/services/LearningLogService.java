package com.chess.chessapi.services;

import com.chess.chessapi.entities.Course;
import com.chess.chessapi.entities.LearningLog;
import com.chess.chessapi.entities.Lesson;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.repositories.LearningLogRepository;
import com.chess.chessapi.viewmodels.LearningLogCreateViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LearningLogService {
    @Autowired
    private LearningLogRepository learningLogRepository;

    public long create(LearningLogCreateViewModel learningLogCreateViewModel,long userId){
        LearningLog learningLog = new LearningLog();
        Course course = new Course();
        course.setCourseId(learningLogCreateViewModel.getCourseId());
        learningLog.setCourse(course);
        learningLog.setFinishedDate(learningLogCreateViewModel.getFinishedDate());
        Lesson lesson = new Lesson();
        lesson.setLessonId(learningLogCreateViewModel.getLessonId());
        learningLog.setLesson(lesson);
        User user = new User();
        user.setUserId(userId);
        learningLog.setUser(user);

        LearningLog savedLearningLog = this.learningLogRepository.save(learningLog);
        return savedLearningLog.getLearninglogId();
    }

    public List<Long> getAllByCourseId(long courseId,long userId){
        return this.learningLogRepository.findAllByCourseIdAndUserId(courseId,userId);
    }

    public void deleteAllByLessonId(long lessonId){
        this.learningLogRepository.deleteAllByLessonId(lessonId);
    }
}
