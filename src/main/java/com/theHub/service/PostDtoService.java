package com.theHub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.theHub.mapper.PostMapper;
import com.theHub.model.dto.postDto.PostContentDto;
import com.theHub.model.dto.postDto.PostForumResponseDto;
import com.theHub.model.dto.postDto.PostThreadResponseDto;
import com.theHub.model.dto.postDto.PostUpdateDto;
import com.theHub.model.entities.ForumUser;
import com.theHub.model.entities.ForumUserPk;
import com.theHub.model.entities.Post;
import com.theHub.model.entities.PostUser;
import com.theHub.model.entities.PostUserPk;
import com.theHub.model.persist.dao.ForumInteractionDao;
import com.theHub.model.persist.dao.PostDao;
import com.theHub.model.persist.dao.PostInteractionDao;

@Service
public class PostDtoService {
	
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private TimeFormatService timeFormatService;
	
	@Autowired
	private PostInteractionDao postInteractionDao;
	
	@Autowired
	private PostDao postDao;
	
	@Autowired
	private ForumInteractionDao forumInteractionDao;
	
	public Post getPostFromPostContentDto(PostContentDto postContentDto) {
		
		Post post = postMapper.postContentDtoToPost(postContentDto);
		return post;
	}
	
	public Post getPostFromPostUpdateDto(PostUpdateDto postUpdateDto) {
		
		Post post = postMapper.postUpdateDtoToPost(postUpdateDto);
		return post;
	}

	public PostForumResponseDto getPostForumResponseDtoFromPost(Post post, Long loggedUserId) {
		
		PostForumResponseDto dto = postMapper.postToPostForumResponseDto(post);
		PostUserPk relationId = new PostUserPk(post.getId(), loggedUserId);
		dto.setElapsedTime(timeFormatService.calculateTimeElapsed(post.getPostDate()));
		PostUser relation = postInteractionDao.getPostRelationById(relationId);
		if (relation == null) {
			dto.setLiked(false);
			dto.setSaved(false);
		} else {
			dto.setLiked(relation.isLiked());
			dto.setSaved(relation.isSaved());
		}
		
		ForumUserPk loggedUserForumRelationPk = new ForumUserPk(post.getForum().getId(), loggedUserId);
		ForumUser loggedUserForumRelation = forumInteractionDao.getForumRelationById(loggedUserForumRelationPk);
		
		ForumUserPk userForumRelationPk = new ForumUserPk(post.getForum().getId(), post.getAuthor().getId());
		ForumUser userForumRelation = forumInteractionDao.getForumRelationById(userForumRelationPk);
		
		if(loggedUserForumRelation != null && loggedUserForumRelation.isBanned()) {
			dto.setBanned(true);
			dto.setHasPermissons(false);
		} else {
			dto.setBanned(false);
			if((loggedUserForumRelation != null && loggedUserForumRelation.isAdmin() && 
					((userForumRelation == null && post.getForum().getCreator().getId() != post.getAuthor().getId()) || 
					userForumRelation != null && !userForumRelation.isAdmin())) || 
					post.getForum().getCreator().getId() == loggedUserId) 
				dto.setHasPermissons(true);
			else
				dto.setHasPermissons(false);
		}
		
		
		dto.setReplied(postDao.hasReplies(post.getId()));
		return dto;
	}
	
	//OBTENER POSTS DE UN FORO
	public Page<PostForumResponseDto> getPostResponseDtosFromPosts(Page<Post> posts, Long loggedUserId){
		Page<PostForumResponseDto> postResponseDtos = posts
				.map(post -> getPostForumResponseDtoFromPost(post, loggedUserId));
		return postResponseDtos;
	}
	
	public PostThreadResponseDto getPostThreadResponseDtoFromPost(Post post, Long loggedUserId) {
		PostThreadResponseDto dto = postMapper.postToPostThreadResponseDto(post);
		PostUserPk relationId = new PostUserPk(post.getId(), loggedUserId);
		dto.setElapsedTime(timeFormatService.calculateTimeElapsed(post.getPostDate()));
		PostUser relation = postInteractionDao.getPostRelationById(relationId);
		if (relation == null) {
			dto.setLiked(false);
			dto.setSaved(false);
		} else {
			dto.setLiked(relation.isLiked());
			dto.setSaved(relation.isSaved());
		}
		
		ForumUserPk loggedUserForumRelationPk = new ForumUserPk(post.getForum().getId(), loggedUserId);
		ForumUser loggedUserForumRelation = forumInteractionDao.getForumRelationById(loggedUserForumRelationPk);
		
		ForumUserPk userForumRelationPk = new ForumUserPk(post.getForum().getId(), post.getAuthor().getId());
		ForumUser userForumRelation = forumInteractionDao.getForumRelationById(userForumRelationPk);
		
		if(loggedUserForumRelation != null && loggedUserForumRelation.isBanned()) {
			dto.setBanned(true);
			dto.setHasPermissons(false);
		} else {
			dto.setBanned(false);
			if((loggedUserForumRelation != null && loggedUserForumRelation.isAdmin() && 
					((userForumRelation == null && post.getForum().getCreator().getId() != post.getAuthor().getId()) || 
					userForumRelation != null && !userForumRelation.isAdmin())) || 
					post.getForum().getCreator().getId() == loggedUserId) 
				dto.setHasPermissons(true);
			else
				dto.setHasPermissons(false);
		}
		
		return dto;
	}
}