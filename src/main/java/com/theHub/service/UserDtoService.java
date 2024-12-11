package com.theHub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.theHub.mapper.UserMapper;
import com.theHub.model.dto.userDto.UserContentDto;
import com.theHub.model.dto.userDto.UserContentResponseDto;
import com.theHub.model.dto.userDto.UserForumResponseDto;
import com.theHub.model.dto.userDto.UserProfileResponseDto;
import com.theHub.model.entities.Follow;
import com.theHub.model.entities.FollowPk;
import com.theHub.model.entities.ForumUser;
import com.theHub.model.entities.ForumUserPk;
import com.theHub.model.entities.Post;
import com.theHub.model.entities.UserEntity;
import com.theHub.model.persist.dao.FollowDao;
import com.theHub.model.persist.dao.ForumInteractionDao;
import com.theHub.model.persist.dao.PostDao;

@Service
public class UserDtoService {

	@Autowired
	private ForumInteractionDao forumInteractionDao;

	@Autowired
	private FollowDao followDao;
	
	@Autowired
	private PostDao postDao;

	@Autowired
	private UserMapper userMapper;

	public UserEntity getUserFromUserContentDto(UserContentDto userContentDto) {
		UserEntity user = userMapper.userContentDtoToUser(userContentDto);
		return user;
	}

	// OBTENER LOS USUARIOS JUNTO CON SUS RELACIONES, TANTO CON EL FORO COMO CON EL USUARIO PARA MOSTRARLO CON COLORES
	public UserForumResponseDto getUserForumResponseDtoFromUser(Long forumId, UserEntity user, Long loggedUserId) {
		UserForumResponseDto dto = userMapper.userToUserForumResponseDto(user);

		FollowPk followPk = new FollowPk(user.getId(), loggedUserId);
		Follow follow = followDao.getFollowById(followPk);
		if (follow == null)
			dto.setFollowed(false);
		else
			dto.setFollowed(true);

		ForumUserPk forumRelationPk = new ForumUserPk(forumId, user.getId());
		ForumUser forumRelation = forumInteractionDao.getForumRelationById(forumRelationPk);
		if (forumRelation == null)
			dto.setAdmin(false);
		else
			dto.setAdmin(forumRelation.isAdmin());
		
		return dto;
	}
	
	public Page<UserForumResponseDto> getUserForumResponseDtosFromUsers(
			Page<UserEntity> users, Long forumId, Long loggedUserId) {
		Page<UserForumResponseDto> userDtos = users
				.map(user -> getUserForumResponseDtoFromUser(forumId, user, loggedUserId));
		return userDtos;
	}
	
	public Page<UserForumResponseDto> getThreadUserForumResponseDtosFromUsers(
			Page<UserEntity> users, Long postId, Long loggedUserId) {
		Post post = postDao.getPostById(postId);
		Long forumId = post.getForum().getId();
		Page<UserForumResponseDto> userDtos = users
				.map(user -> getUserForumResponseDtoFromUser(forumId, user, loggedUserId));
		return userDtos;
	}

	public UserContentDto getUserContentDtoFromUser(UserEntity user) {
		UserContentDto dto = userMapper.userToUserContentDto(user);
		return dto;
	}
	
	public UserContentResponseDto getUserContentResponseDtoFromUser(UserEntity user) {
		UserContentResponseDto dto = userMapper.userToUserContentResponseDto(user);
		return dto;
	}

	public Page<UserContentResponseDto> getUserContentResponseDtosFromUsers(Page<UserEntity> users) {
		Page<UserContentResponseDto> userDtos = users.map(user -> getUserContentResponseDtoFromUser(user));
		return userDtos;
	}
	
	public UserProfileResponseDto getUserProfileResponseDtoFromUser(UserEntity user, Long loggedUserId) {
		UserProfileResponseDto  dto = userMapper.userToUserProfileResponseDto(user);
		FollowPk followPk = new FollowPk(user.getId(), loggedUserId);
		Follow follow = followDao.getFollowById(followPk);
		if (follow == null)
			dto.setFollowed(false);
		else
			dto.setFollowed(true);
		return dto;
	}
	
	public Page<UserProfileResponseDto> getUserProfileResponseDtosFromUser(Page<UserEntity> users, Long loggedUserId) {
		Page<UserProfileResponseDto> userDtos = users.map(user -> getUserProfileResponseDtoFromUser(user, loggedUserId));
		return userDtos;
	}
}