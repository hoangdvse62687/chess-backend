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

    @Query(value = "Update lesson l Set l.name = ?2 where id = ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void update(long lessonId,String name);

    @Query(value = "Select l.id,l.name,l.created_date,l.type" +
            " From lesson l where l.owner = ?2 and l.name like ?1"
            ,countQuery = "Select count(l.id) From lesson l where l.owner = ?2 and l.name like ?1"
            ,nativeQuery = true)
    Page<Object> findAllByOwner(Pageable pageable,String name, long userId);
}
