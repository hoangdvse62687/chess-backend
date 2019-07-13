package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.ExerciseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ExerciseLogRepository extends JpaRepository<ExerciseLog,Long> {

    @Query(value = "Update exercise_log Set is_passed = ?2 where id = ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void update(long exerciseLogId,boolean isPassed);

    @Query(value = "Select If(count(user_id) > 0,user_id,0) From exercise_log where id = ?1",nativeQuery = true)
    Long findUserIdById(long id);
}
