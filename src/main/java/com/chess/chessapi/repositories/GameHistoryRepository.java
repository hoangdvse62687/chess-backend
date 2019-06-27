package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.GameHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameHistoryRepository extends JpaRepository<GameHistory,Long> {
    @Query(value = "Select * from game_history where user_id = ?1",
            countQuery = "Select count(id) from game_history where user_id = ?1",
            nativeQuery = true)
    Page<GameHistory> findAllByUserId(Pageable pageable, long userId);
}
