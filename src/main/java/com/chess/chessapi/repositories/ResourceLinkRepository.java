package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.ResourceLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ResourceLinkRepository extends JpaRepository<ResourceLink,Long> {

    @Query(value = "Select link From resource_link where owner = ?1 order by created_date desc",nativeQuery = true)
    List<String> findAllByUserId(long userId);

    @Modifying
    @Transactional
    @Query(value = "Delete From resource_link where id = ?1",nativeQuery = true)
    void remove(long id);
}
