package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.ExerciseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseLogRepository extends JpaRepository<ExerciseLog,Long> {

}
