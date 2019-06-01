package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.CategoryHasCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryHasCourseRepository extends JpaRepository<CategoryHasCourse,Long> {
}
