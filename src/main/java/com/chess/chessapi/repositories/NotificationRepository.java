package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Notification;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    @Query(value = "Select * From notification n where n.role_target = ?1",
    countQuery = "Select count(n.id) from notification n where n.role_target = ?1",
    nativeQuery = true)
    Page<Notification> findAllByRoleWithPagination(Pageable pageable,String role);

    @Query(value = "Select * From notification n where n.role_target = ?1 and n.object_id = ?2",
            countQuery = "Select count(n.id) from notification n where n.role_target = ?1 and n.object_id = ?2",
            nativeQuery = true)
    @Cacheable
    Page<Notification> findAllByRoleAndObjectIdWithPagination(Pageable pageable,String role,String objectId);
}
