package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "Select u.id,u.email,u.role From user u where u.email = ?1",nativeQuery = true)
    Optional<Object> findByEmailCustom(String email);

    @Query(value = "Select u.id,u.email,u.role From user u where u.id = ?1",nativeQuery = true)
    Optional<Object> findByIdCustom(long id);

    @Query(value = "Select u.id,u.email,u.role,u.avatar,u.full_name,u.gender,u.created_date,u.is_active " +
            "From user u WHERE u.full_name like ?1",
            countQuery = "Select count(u.id) from user u WHERE u.full_name like ?1",
            nativeQuery = true)
    Page<Object> findAllByFullNameCustom(Pageable pageable,String fullName);

    @Query(value = "Select u.id,u.email,u.role,u.avatar,u.full_name,u.gender,u.created_date,u.is_active " +
            "From user u WHERE u.full_name like ?1 and u.role = ?2",
            countQuery = "Select count(u.id) from user u WHERE u.full_name like ?1 and u.role = ?2",
            nativeQuery = true)
    Page<Object> findAllByFullNameSortByRoleCustom(Pageable pageable,String fullName,String role);

    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "Update user u Set u.is_active = ?2 Where u.id = ?1"
            ,nativeQuery = true)
    void updateStatus(long id,int isActive);
}
