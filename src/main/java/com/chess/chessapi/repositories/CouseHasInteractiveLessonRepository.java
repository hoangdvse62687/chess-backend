package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.CouseHasInteractiveLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouseHasInteractiveLessonRepository extends JpaRepository<CouseHasInteractiveLesson,Long> {
}
