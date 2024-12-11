package com.theHub.model.persist.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.theHub.exception.AppException;
import com.theHub.model.entities.Forum;
import com.theHub.model.entities.Post;
import com.theHub.model.entities.UserEntity;
import com.theHub.model.persist.dao.ForumDao;
import com.theHub.model.persist.dao.PostDao;
import com.theHub.model.persist.dao.UserDao;
import com.theHub.model.persist.repository.PostRepository;

import jakarta.transaction.Transactional;

@Service
public class PostDaoImpl implements PostDao{

	@Autowired
	private PostRepository postRep;
	
	@Autowired
	private ForumDao forumDao;
	
	@Autowired 
	private UserDao userDao;
	
	//RELLENA LOS CAMPOS COMUNES A POST Y RESPUESTA
	private Post addDefaultContent(Post post, Long loggedUserId, Long forumId) {
		
		UserEntity loggedUser = userDao.getUserById(loggedUserId);
		Forum forum = forumDao.getForumById(forumId);
		
		post.setAuthor(loggedUser);
		post.setForum(forum);
		post.setLikes(0);
		post.setSaveds(0);
		
		return post;
	}

	//CREA UN POST
	@Override
	public Post createPost(Post post, Long loggedUserId, Long forumId) {
		
		Post filledPost = addDefaultContent(post, loggedUserId, forumId);
		Post createdPost = postRep.save(filledPost);
		return createdPost;
	}
	
	//CREA UNA RESPUESTA
	@Override
	public Post createReply(Post post, Long loggedUserId, Long forumId, Long repliedPostId) {
		Post filledPost = addDefaultContent(post, loggedUserId, forumId);
		Post repliedPost = postRep.findById(repliedPostId)
				.orElseThrow(() -> new AppException("No se ha podido recuperar el post al que se pretende responder", HttpStatus.NOT_FOUND));
		
		filledPost.setRepliedPost(repliedPost);
		Post createdReply = postRep.save(filledPost);
		return createdReply;
	}

	//NO SÉ LA UTILIDAD
	@Override
	public Post getPostById(Long postId) {

		Post post = postRep.findById(postId)
				.orElseThrow(() -> new AppException("No se ha encontrado el post", HttpStatus.NOT_FOUND));
		return post;
	}

	//GESTIONAR SEGURIDAD CON JWT
	@Override
	public Post updatePost(Post post) {
		
		if(post.getId() == null)
			throw new AppException("El id del post a actualizar no puede ser nulo", HttpStatus.BAD_REQUEST);
		
		Post savedPost = postRep.findById(post.getId())
				.orElseThrow(() -> new AppException("No se ha encontrado el post", HttpStatus.NOT_FOUND));
		
		savedPost.setTitle(post.getTitle());
		savedPost.setText(post.getText());
		
		Post updatedPost = postRep.save(savedPost);
		
		return updatedPost;
	}

	@Override
	@Transactional
	public void deletePost(Long postId) {
		Post deletedPost = postRep.findById(postId)
				.orElseThrow(() -> new AppException("No se ha encontrado el post", HttpStatus.NOT_FOUND));
		
		Forum forum = deletedPost.getForum();
		List<Post> replies = deletedPost.getReplies();
		
		forum.setInteractions(forum.getInteractions() - (deletedPost.getLikes() + deletedPost.getSaveds()));
		if (!replies.isEmpty()) 
			replies.forEach(reply -> forum.setInteractions(forum.getInteractions() - (reply.getLikes() + reply.getSaveds())));
		
		forumDao.updateForum(forum);
		postRep.deleteById(postId);
	}
	
	@Override
	public Page<Post> getPostsByForumId(Long forumId, Pageable pageable) {
		
		Page<Post> forumPosts = postRep.findPostsByForumId(forumId, pageable);
		return forumPosts;
	}

	@Override
	public Page<Post> getPostsByRepliedPostId(Long repliedPostId, Pageable pageable) {
		
		Page<Post> replies = postRep.findPostsByRepliedPostId(repliedPostId, pageable);
		return replies;
	}

	@Override
	public Page<Post> getPostsByUserId(Long userId, Pageable pageable) {
		
		Page<Post> userPosts = postRep.findPostsByUserId(userId, pageable);
		return userPosts;
	}

	@Override
	public boolean hasReplies(Long postId) {
		
		boolean hasReplies = postRep.hasReplies(postId); 
		return hasReplies;
	}

}