package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.LearningLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface LearningLogRepository extends JpaRepository<LearningLog,Long> {
    @Modifying
    @Transactional
    @Query(value = "Insert into learning_log (user_id,course_id,lesson_id,finished_date) " +
            "VALUES (?1,?2,?3,?4)",nativeQuery = true)
    void create(long userId, long courseId, long lessonId, Timestamp finishedDate);

    @Query(value = "Select lesson_id From learning_log where course_id = ?1 and user_id = ?2",nativeQuery = true)
    List<Long> findAllByCourseIdAndUserId(long courseId,long userId);
}
