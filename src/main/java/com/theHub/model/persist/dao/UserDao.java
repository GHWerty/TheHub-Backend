package com.theHub.model.persist.dao;

import com.theHub.model.entities.UserEntity;

public interface UserDao {

	public UserEntity getUserById(Long userId);
	public UserEntity updateUser(UserEntity user, Long loggedUserId);
	public UserEntity getUserByNick(String nick);
	public Long getIdByCredentials(String nick, String password);
	public Long createUser(String nick, String email, String password);
	public UserEntity updateCredentials(Long loggedUserId, String email, String password);
}