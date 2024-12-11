package com.theHub.model.persist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.theHub.model.entities.Follow;
import com.theHub.model.entities.FollowPk;
import com.theHub.model.entities.UserEntity;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowPk>{
	

    @Query("SELECT f.follower FROM Follow f WHERE f.followed.id = :userId")
    Page<UserEntity> findFollowersByUserId(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT f.follower FROM Follow f WHERE f.followed.id = :userId AND f.follower.nick LIKE %:nick%")
    Page<UserEntity> findFollowersByUserIdAndNickContaining(@Param("userId") Long userId, @Param("nick") String nick, Pageable pageable);
    
    @Query("SELECT f.followed FROM Follow f WHERE f.follower.id = :userId")
    Page<UserEntity> findFollowedUsersByUserId(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT f.followed FROM Follow f WHERE f.follower.id = :userId AND f.followed.nick LIKE %:nick%")
    Page<UserEntity> findFollowedUsersByUserIdAndNickContaining(@Param("userId") Long userId, @Param("nick") String nick, Pageable pageable);

}