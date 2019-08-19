package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise,Long> {
    @Query(value = "Update exercise Set question = ?2,answer = ?3  where id = ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void update(long lessonId,String question,String answer);
}
