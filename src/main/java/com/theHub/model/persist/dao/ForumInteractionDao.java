package com.theHub.model.persist.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.theHub.model.entities.Forum;
import com.theHub.model.entities.ForumUser;
import com.theHub.model.entities.ForumUserPk;
import com.theHub.model.entities.UserEntity;

public interface ForumInteractionDao {

	public ForumUser getForumRelationById(ForumUserPk forumUserPk);
	
	public String followForum(Long forumId, Long loggedUserId);
	public String unfollowForum(Long forumId, Long loggedUserId);
	public String updateAdminRole(Long loggedUserId, Long forumId, Long userId, boolean grantAdmin);
	public String updateBan(Long loggedUserId, Long forumId, Long userId, boolean banned);
	
	public Page<UserEntity> getForumUsersByForumId(Long forumId, Pageable pageable);
	public Page<UserEntity> getForumUsersByForumIdAndNick(Long forumId, String nick, Pageable pageable);
	public Page<UserEntity> getBannedUsersByForumId(Long forumId, Pageable pageable);
	public Page<UserEntity> getBannedUsersByForumIdAndNick(Long forumId,String nick, Pageable pageable);
	
	public Page<Forum> getFollowedForumsByUser(Long userId, Pageable pageable);
	public Page<Forum> getForumsByCreatorId(Long userId, Pageable pageable);
	public Page<Forum> getFollowedForumsByUserIdAndTitle(Long userId, String title, Pageable pageable);
	public Page<Forum> getForumsByCreatorIdAndTitle(Long userId, String title, Pageable pageable);
	public UserEntity getForumCreator(Long forumId);
	
}