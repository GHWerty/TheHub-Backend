package com.theHub.model.persist.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.theHub.model.entities.Post;
import com.theHub.model.entities.PostUser;
import com.theHub.model.entities.PostUserPk;
import com.theHub.model.entities.UserEntity;

public interface PostInteractionDao {
	
	public PostUser getPostRelationById(PostUserPk postUserPk);
	public Post interactionHandler(Long postId, Long loggedUserId, String action);
		
	public Page<Post> getPostsSavedByUserId(Long loggedUserId, Pageable pageable);
	public Page<UserEntity> getThreadUsersByPostId(Long postId, Pageable pageable);
	public Page<UserEntity> getThreadUsersByPostIdAndNick(Long postId, String nick, Pageable pageable);
	//Devuelve todos los posts relevantes para el usuario en página principal
	public Page<Post> getProfilePosts(Long loggedUserId, Pageable pageable);
	
}