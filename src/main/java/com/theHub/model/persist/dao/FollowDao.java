package com.theHub.model.persist.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.theHub.model.entities.Follow;
import com.theHub.model.entities.FollowPk;
import com.theHub.model.entities.UserEntity;

public interface FollowDao {

	public Follow followUser(Long userId, Long loggedUserId);
	public UserEntity unfollowUser(Long userId, Long loggedUserId);
	public Follow getFollowById(FollowPk followPk);
	
	public Page<UserEntity> getFollowersByLogedUserId(Long id, Pageable pageable);
	public Page<UserEntity> getFollowedUsersByLogedUserId(Long id, Pageable pageable);
	
	public Page<UserEntity> getFollowersByLogedUserIdAndNick(Long id, String nick, Pageable pageable);
	public Page<UserEntity> getFollowedUsersByLogedUserIdAndNick(Long id, String nick, Pageable pageable);
	
}