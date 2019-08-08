package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface LessonRepository extends JpaRepository<Lesson,Long> {
    @Query(value = "Select l.owner From lesson l where l.id = ?1",nativeQuery = true)
    Long findLessonAuthorByLessonId(long lessonId);

    @Query(value = "Update lesson l Set l.name = ?2,l.description = ?3 where id = ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void update(long lessonId,String name,String description);
}
