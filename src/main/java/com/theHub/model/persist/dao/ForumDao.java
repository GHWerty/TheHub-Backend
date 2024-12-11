package com.theHub.model.persist.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.theHub.model.entities.Forum;

public interface ForumDao {
	
	public Forum createForum(Forum forum, Long loggedUserId);
	public Forum getForumById(Long forumId);
	//PARA EL TRIGGER DE LAS INTERACCIONES
	public Forum updateForum(Forum forum);
	public void deleteForumById(Long forumId);
	public Page<Forum> getForumsSortedAndPaginated(Pageable pageable);
	public Page<Forum> getForumsByTitle(String title, Pageable pageable);
	
}
