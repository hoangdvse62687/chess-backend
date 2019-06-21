package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.InteractiveLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface InteractiveLessonRepository extends JpaRepository<InteractiveLesson,Long> {
    @Query(value = "Update interactive_lesson Set init_code = ?2  where id = ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void update(long iLessonId,String initCode);
}
