package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Notification;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    @Query(value = "Select * From notification n where n.role_target = ?1",
    countQuery = "Select count(n.id) from notification n where n.role_target = ?1",
    nativeQuery = true)
    Page<Notification> findAllByRoleWithPagination(Pageable pageable,long role);

    @Query(value = "Select * From notification n where n.role_target = ?1 and n.user_id = ?2",
            countQuery = "Select count(n.id) from notification n where n.role_target = ?1 and n.user_id = ?2",
            nativeQuery = true)
    @Cacheable
    Page<Notification> findAllByRoleAndObjectIdWithPagination(Pageable pageable,long role,String userId);

    @Query(value = "Update notification Set is_viewed = 1 where id in ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void updateIsViewed(List<Long> listIds);
}
