package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.InteractiveLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteractiveLessonRepository extends JpaRepository<InteractiveLesson,Long> {
}
