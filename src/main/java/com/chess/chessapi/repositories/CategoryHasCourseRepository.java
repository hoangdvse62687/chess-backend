package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.CategoryHasCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CategoryHasCourseRepository extends JpaRepository<CategoryHasCourse,Long> {
    @Modifying
    @Transactional
    @Query(value = "Insert into category_has_course (category_id,course_id) " +
            "VALUES (?1,?2)"
            ,nativeQuery = true)
    void create(long categoryId,long courseId);
}
