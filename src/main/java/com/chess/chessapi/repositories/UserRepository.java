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

    @Query(value = "Select u.id,u.email,u.role_id,u.is_active From users u where u.email = ?1",nativeQuery = true)
    Optional<Object> findByEmailCustom(String email);

    @Query(value = "Select u.id,u.email,u.role_id,u.is_active From users u where u.id = ?1",nativeQuery = true)
    Optional<Object> findByIdCustom(long id);

    @Query(value = "Select u.id,u.email,u.role_id,u.is_active,u.avatar,u.full_name,u.created_date " +
            "From users u WHERE u.full_name like ?1",
            countQuery = "Select count(u.id) from user u WHERE u.email like ?1",
            nativeQuery = true)
    Page<Object> findAllByFullNameCustom(Pageable pageable,String email);

    @Query(value = "Select u.id,u.email,u.role_id,u.is_active,u.avatar,u.full_name,u.created_date " +
            "From users u WHERE u.full_name like ?1 and u.role_id = ?2",
            countQuery = "Select count(u.id) from user u WHERE u.email like ?1 and u.role_id = ?2",
            nativeQuery = true)
    Page<Object> findAllByFullNameSortByRoleCustom(Pageable pageable,String email,String role);

    @Query(value = "Select u.id,u.email,u.role_id,u.is_active,u.avatar,u.full_name,u.created_date " +
            "From users u WHERE u.is_active = ?1 and u.email = ?2",
            countQuery = "Select count(u.id) from user u WHERE u.is_active = ?1 and u.email = ?2",
            nativeQuery = true)
    Page<Object> findAllByStatus(Pageable pageable,boolean status,String email);

    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "Update users u Set u.is_active = ?2 Where u.id = ?1"
            ,nativeQuery = true)
    void updateStatus(long id,int isActive);

    @Modifying
    @Transactional
    @Query(value = "Update users u Set u.full_name = ?2,u.achievement = ?3 Where u.id = ?1"
            ,nativeQuery = true)
    void updateProfile(long id,String name,String achievement);
}
