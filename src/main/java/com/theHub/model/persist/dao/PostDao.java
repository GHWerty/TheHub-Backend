package com.theHub.model.persist.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.theHub.model.entities.Post;

public interface PostDao {
	
	public Post createPost(Post post, Long loggedUserId, Long forumId);
	public Post createReply(Post post, Long loggedUserId, Long forumId, Long repliedPostId);
	public Post getPostById(Long postId);
	public Post updatePost(Post post);
	public void deletePost(Long postId);
	public boolean hasReplies(Long postId);
	
	public Page<Post> getPostsByForumId(Long forumId, Pageable pageable);
	public Page<Post> getPostsByRepliedPostId(Long repliedPostId, Pageable pageable);
	public Page<Post> getPostsByUserId(Long userId, Pageable pageable);
	
}