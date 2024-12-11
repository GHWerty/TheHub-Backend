package com.theHub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.theHub.controller.util.ControllerResponseHandler;
import com.theHub.model.dto.postDto.PostContentDto;
import com.theHub.model.dto.postDto.PostForumResponseDto;
import com.theHub.model.dto.postDto.PostThreadResponseDto;
import com.theHub.model.dto.postDto.PostUpdateDto;
import com.theHub.model.entities.Post;
import com.theHub.model.persist.dao.PostDao;
import com.theHub.model.persist.dao.PostInteractionDao;
import com.theHub.service.PostDtoService;

@RestController
@RequestMapping("/posts")
public class PostRestController {
	
	@Autowired
	private PostInteractionDao postInteractionDao;
	
	@Autowired
	private PostDtoService postDtoService;
	
	@Autowired
	private PostDao postDao;
	
	@Autowired
	private ControllerResponseHandler responseHandler;
	
	//OBTENER POSTS DE UN FORO
	@GetMapping("/forum/{id}")
	public ResponseEntity<?> getForumPosts(@PathVariable Long id, Pageable pageable){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<Post> forumPosts = postDao.getPostsByForumId(id, pageable);
			Page<PostForumResponseDto> forumPostsDtos = postDtoService.getPostResponseDtosFromPosts(forumPosts, loggedUserId);
			responseContent.put("result", forumPostsDtos);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping
	public ResponseEntity<?> getProfilePosts(Pageable pageable){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<Post> profilePosts = postInteractionDao.getProfilePosts(loggedUserId, pageable);
			Page<PostForumResponseDto> profilePostsDtos = postDtoService.getPostResponseDtosFromPosts(profilePosts, loggedUserId);
			responseContent.put("result", profilePostsDtos);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	//OBTENER LOS POSTS DE UN USUARIO
	@GetMapping("/user/{id}")
	public ResponseEntity<?> getUserPosts(@PathVariable Long id, Pageable pageable){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<Post> userPosts = postDao.getPostsByUserId(id, pageable);
			Page<PostForumResponseDto> userPostDtos = postDtoService.getPostResponseDtosFromPosts(userPosts, loggedUserId);
			responseContent.put("result", userPostDtos);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping("/saveds")
	public ResponseEntity<?> getUserSavedPosts(Pageable pageable){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<Post> userPosts = postInteractionDao.getPostsSavedByUserId(loggedUserId, pageable);
			Page<PostForumResponseDto> userPostDtos = postDtoService.getPostResponseDtosFromPosts(userPosts, loggedUserId);
			responseContent.put("result", userPostDtos);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	//OBTENER RESPUESTAS DE UN POST
	@GetMapping("/replies/{id}")
	public ResponseEntity<?> getPostReplies(@PathVariable Long id, Pageable pageable){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<Post> postReplies = postDao.getPostsByRepliedPostId(id, pageable);
			Page<PostForumResponseDto> postReplyDtos = postDtoService.getPostResponseDtosFromPosts(postReplies, loggedUserId);
			responseContent.put("result", postReplyDtos);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getThreadPost(@PathVariable Long id){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Post post = postDao.getPostById(id);
			PostThreadResponseDto postDto = postDtoService.getPostThreadResponseDtoFromPost(post, loggedUserId);
			responseContent.put("result", postDto);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	//PUBLICAR UN POST
	@PostMapping
	public ResponseEntity<?> createPost(@RequestBody PostContentDto postContentDto) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Post post = postDtoService.getPostFromPostContentDto(postContentDto);
			Post createdPost = postDao.createPost(post, loggedUserId, postContentDto.forumId());
			PostForumResponseDto createdPostDto = postDtoService.getPostForumResponseDtoFromPost(createdPost, loggedUserId);
			responseContent.put("result", createdPostDto);
			HttpStatus httpStatus = HttpStatus.CREATED;
			return httpStatus;
		});
	}
	
	//CREAR UNA RESPUESTA
	@PostMapping("/{id}")
	public ResponseEntity<?> replyPost(@RequestBody PostContentDto postContentDto, @PathVariable Long id) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Post post = postDtoService.getPostFromPostContentDto(postContentDto);
			Post createdPost = postDao.createReply(post, loggedUserId, postContentDto.forumId(), id);
			PostForumResponseDto createdPostDto = postDtoService.getPostForumResponseDtoFromPost(createdPost, loggedUserId);
			responseContent.put("result", createdPostDto);
			HttpStatus httpStatus = HttpStatus.CREATED;
			return httpStatus;
		});
	}
	
	//INTERACIONES
	@PostMapping("/{id}/{action}")
	public ResponseEntity<?> postInteractions(@PathVariable Long id, @PathVariable String action) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Post interactedPost = postInteractionDao.interactionHandler(id, loggedUserId, action);
			PostForumResponseDto interactedPostDto = postDtoService.getPostForumResponseDtoFromPost(interactedPost, loggedUserId);
			responseContent.put("result", interactedPostDto);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	//ACTUALIZAR POST
	@PutMapping
	public ResponseEntity<?> updatePost(@RequestBody PostUpdateDto postUpdateDto) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Post post = postDtoService.getPostFromPostUpdateDto(postUpdateDto);
			Post updatedPost = postDao.updatePost(post);
			PostForumResponseDto updatedPostDto = postDtoService.getPostForumResponseDtoFromPost(updatedPost, loggedUserId);
			responseContent.put("result", updatedPostDto);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePost(@PathVariable Long id) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			postDao.deletePost(id);
			responseContent.put("result", "Post eliminado con éxito");
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}

}