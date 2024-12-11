package com.theHub.model.persist.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.theHub.exception.AppException;
import com.theHub.model.entities.Follow;
import com.theHub.model.entities.FollowPk;
import com.theHub.model.entities.UserEntity;
import com.theHub.model.persist.dao.FollowDao;
import com.theHub.model.persist.dao.UserDao;
import com.theHub.model.persist.repository.FollowRepository;

@Service
public class FollowDaoImpl implements FollowDao{
	
	@Autowired
	private FollowRepository followRep;
	
	@Autowired
	private UserDao userDao;

	@Override
	public Follow followUser(Long userId, Long loggedUserId) {
		
		FollowPk followPk = new FollowPk(userId, loggedUserId);
		Follow savedFollow =followRep.findById(followPk).orElse(null);
		
		if(savedFollow != null)
			throw new AppException("El usuario logeado ya sigue al usuario", HttpStatus.FORBIDDEN);
		
		UserEntity followedUser = userDao.getUserById(userId);
		UserEntity loggedUser = userDao.getUserById(loggedUserId);
		Follow newFollow = Follow.builder()
				.followPk(followPk)
				.followed(followedUser)
				.follower(loggedUser)
				.build();
		
		Follow createdFollow = followRep.save(newFollow);
		return createdFollow;
	}

	@Override
	public UserEntity unfollowUser(Long userId, Long loggedUserId) {
		
		FollowPk followPk = new FollowPk(userId, loggedUserId);
		if (!followRep.existsById(followPk))
			throw new AppException("El usuario logeado no sigue al usuario", HttpStatus.FORBIDDEN);
		
		followRep.deleteById(followPk);
		UserEntity unfollowedUser = userDao.getUserById(userId);
		return unfollowedUser;
	}

	@Override
	public Follow getFollowById(FollowPk followPk) {
		
		Follow follow = followRep.findById(followPk).orElse(null);
		return follow;
	}

	@Override
	public Page<UserEntity> getFollowersByLogedUserId(Long id, Pageable pageable) {
		
		Page<UserEntity> follows = followRep.findFollowersByUserId(id, pageable);
		return follows;
	}
	
	@Override
	public Page<UserEntity> getFollowersByLogedUserIdAndNick(Long id, String nick, Pageable pageable) {
		Page<UserEntity> follows = followRep.findFollowersByUserIdAndNickContaining(id, nick, pageable);
		return follows;
	}

	@Override
	public Page<UserEntity> getFollowedUsersByLogedUserId(Long id, Pageable pageable) {
		Page<UserEntity> followedUsers = followRep.findFollowedUsersByUserId(id, pageable);
		return followedUsers;
	}

	@Override
	public Page<UserEntity> getFollowedUsersByLogedUserIdAndNick(Long id, String nick, Pageable pageable) {
		Page<UserEntity> followedUsers = followRep.findFollowedUsersByUserIdAndNickContaining(id, nick, pageable);
		return followedUsers;
	}

}
