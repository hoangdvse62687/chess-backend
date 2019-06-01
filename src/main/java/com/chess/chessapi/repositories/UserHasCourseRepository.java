package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.UserHasCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHasCourseRepository extends JpaRepository<UserHasCourse,Long> {
}
