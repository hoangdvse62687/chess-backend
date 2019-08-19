package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.LearningLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LearningLogRepository extends JpaRepository<LearningLog,Long> {
    @Query(value = "Select lesson_id From learning_log where course_id = ?1 and user_id = ?2",nativeQuery = true)
    List<Long> findAllByCourseIdAndUserId(long courseId,long userId);

    @Modifying
    @Transactional
    @Query(value = "Delete From learning_log where lesson_id = ?1",nativeQuery = true)
    void deleteAllByLessonId(long lessonId);

    @Modifying
    @Transactional
    @Query(value = "Delete From learning_log where lesson_id = ?1 and course_id = ?2",nativeQuery = true)
    void deleteAllByLessonIdAndCourseId(long lessonId,long courseId);
}
