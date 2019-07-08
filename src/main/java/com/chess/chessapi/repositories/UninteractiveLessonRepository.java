package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.UninteractiveLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface UninteractiveLessonRepository extends JpaRepository<UninteractiveLesson,Long> {
    @Query(value = "Update uninteractive_lesson Set content = ?2  where id = ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void update(long uiLessonId,String content);
}
