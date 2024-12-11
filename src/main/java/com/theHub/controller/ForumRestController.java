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
import com.theHub.model.dto.forumDto.ForumContentDto;
import com.theHub.model.dto.forumDto.ForumProfileResponseDto;
import com.theHub.model.dto.forumDto.ForumResponseDto;
import com.theHub.model.dto.forumDto.ForumUpdateDto;
import com.theHub.model.entities.Forum;
import com.theHub.model.persist.dao.ForumDao;
import com.theHub.model.persist.dao.ForumInteractionDao;
import com.theHub.service.ForumDtoService;

@RestController
@RequestMapping("/forums")
public class ForumRestController {
	
	@Autowired
	private ForumDtoService forumDtoService;
	
	@Autowired
	private ForumDao forumDao;
	
	@Autowired
	private ForumInteractionDao forumInteractionDao;
	
	@Autowired
	private ControllerResponseHandler responseHandler;
	
	@GetMapping
	public ResponseEntity<?> getForums(Pageable pageable) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<Forum> forums = forumDao.getForumsSortedAndPaginated(pageable);
			Page<ForumResponseDto> forumsDto = 
					forumDtoService.getForumResponseDtosFromForums(forums, loggedUserId);
			responseContent.put("result", forumsDto);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping("/title/{title}")
	public ResponseEntity<?> getForumsByTitle(@PathVariable String title, Pageable pageable) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<Forum> forums = forumDao.getForumsByTitle(title, pageable);
			Page<ForumResponseDto> forumsDto = 
					forumDtoService.getForumResponseDtosFromForums(forums, loggedUserId);
			responseContent.put("result", forumsDto);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping("followed/{id}")
	public ResponseEntity<?> getFollowedForums(@PathVariable Long id, Pageable pageable) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<Forum> followedForums = forumInteractionDao.getFollowedForumsByUser(id, pageable);
			Page<ForumResponseDto> followedForumsDto = 
					forumDtoService.getForumResponseDtosFromForums(followedForums, loggedUserId);
			responseContent.put("result", followedForumsDto);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping("followed/{id}/title/{title}")
	public ResponseEntity<?> getFollowedForumsByTitle(@PathVariable Long id, @PathVariable String title,Pageable pageable) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<Forum> followedForums = forumInteractionDao.getFollowedForumsByUserIdAndTitle(id, title, pageable);
			Page<ForumResponseDto> followedForumsDto = 
					forumDtoService.getForumResponseDtosFromForums(followedForums, loggedUserId);
			responseContent.put("result", followedForumsDto);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping("created/{id}")
	public ResponseEntity<?> getCreatedForums(@PathVariable Long id, Pageable pageable) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<Forum> createdForums = forumInteractionDao.getForumsByCreatorId(id, pageable);
			Page<ForumProfileResponseDto> createdForumsDto = 
					forumDtoService.getForumProfileResponseDtosFromForums(createdForums);
			responseContent.put("result", createdForumsDto);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping("created/{id}/title/{title}")
	public ResponseEntity<?> getCreatedForumsByTitle(@PathVariable Long id, @PathVariable String title, Pageable pageable) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<Forum> createdForums = forumInteractionDao.getForumsByCreatorIdAndTitle(id, title, pageable);
			Page<ForumProfileResponseDto> createdForumsDto = 
					forumDtoService.getForumProfileResponseDtosFromForums(createdForums);
			responseContent.put("result", createdForumsDto);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@PostMapping("/follow/{id}")
	public ResponseEntity<?> followForum(@PathVariable Long id) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			String resultMessage = forumInteractionDao.followForum(id, loggedUserId);
			responseContent.put("result", resultMessage);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@PostMapping("/unfollow/{id}")
	public ResponseEntity<?> unFollowForum(@PathVariable Long id) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			String resultMessage = forumInteractionDao.unfollowForum(id, loggedUserId);
			responseContent.put("result", resultMessage);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;	
		});
	}
	
	@PostMapping
	public ResponseEntity<?> createForum(@RequestBody ForumContentDto forumContentDto) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Forum forum = forumDtoService.getForumFromForumContentDto(forumContentDto);
			Forum createdForum = forumDao.createForum(forum, loggedUserId);
			ForumProfileResponseDto createdForumDto = 
					forumDtoService.getForumProfileResponseDtoFromForum(createdForum);
			responseContent.put("result", createdForumDto);
			HttpStatus httpStatus = HttpStatus.CREATED;
			return httpStatus;
		});
	}
	
	@PostMapping("/forum/{forumId}/ban/{userId}")
	public ResponseEntity<?> banUser(@PathVariable Long forumId, @PathVariable Long userId) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			String result = forumInteractionDao.updateBan(loggedUserId, forumId, userId, true);
			responseContent.put("result", result);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@PostMapping("/forum/{forumId}/unban/{userId}")
	public ResponseEntity<?> unbanUser(@PathVariable Long forumId, @PathVariable Long userId) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			String result = forumInteractionDao.updateBan(loggedUserId, forumId, userId, false);
			responseContent.put("result", result);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getForumById(@PathVariable Long id) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Forum forum = forumDao.getForumById(id);
			ForumResponseDto forumDto = forumDtoService.getForumResponseDtoFromForum(forum, loggedUserId);
			responseContent.put("result", forumDto);
			HttpStatus httpStatus = HttpStatus.CREATED;
			return httpStatus;
		});
	}
	
	@PostMapping("/forum/{forumId}/user/{userId}/admin/{admin}")
	public ResponseEntity<?> grantAdmin(@PathVariable Long forumId, @PathVariable Long userId, @PathVariable boolean admin) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			String resultMessage  = forumInteractionDao.updateAdminRole(loggedUserId, forumId, userId, admin);
			responseContent.put("result", resultMessage);
			HttpStatus httpStatus = HttpStatus.CREATED;
			return httpStatus;
		});
	}
	
	@PutMapping
	public ResponseEntity<?> updateForum(@RequestBody ForumUpdateDto forumUpdateDto) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Forum forum = forumDtoService.getForumFromForumUpdateDto(forumUpdateDto);
			Forum updatedForum = forumDao.updateForum(forum);
			ForumProfileResponseDto updatedForumDto = 
					forumDtoService.getForumProfileResponseDtoFromForum(updatedForum);
			responseContent.put("result", updatedForumDto);
			HttpStatus httpStatus = HttpStatus.CREATED;
			return httpStatus;
		});
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteForum(@PathVariable Long id) {
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			forumDao.deleteForumById(id);
			responseContent.put("result", "Foro eliminado con éxito");
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
}