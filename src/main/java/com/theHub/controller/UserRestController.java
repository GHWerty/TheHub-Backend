package com.theHub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.theHub.controller.util.ControllerResponseHandler;
import com.theHub.model.dto.userDto.CredentialsDto;
import com.theHub.model.dto.userDto.UserContentDto;
import com.theHub.model.dto.userDto.UserForumResponseDto;
import com.theHub.model.dto.userDto.UserProfileResponseDto;
import com.theHub.model.entities.Follow;
import com.theHub.model.entities.UserEntity;
import com.theHub.model.persist.dao.FollowDao;
import com.theHub.model.persist.dao.ForumInteractionDao;
import com.theHub.model.persist.dao.PostInteractionDao;
import com.theHub.model.persist.dao.UserDao;
import com.theHub.service.UserDtoService;

@RestController
@RequestMapping("/users")
public class UserRestController {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private FollowDao followDao;
	
	@Autowired
	private ForumInteractionDao forumInteractionDao;
	
	@Autowired
	private PostInteractionDao postInteractionDao;
	
	@Autowired
	private ControllerResponseHandler responseHandler;
	
	@Autowired
	private UserDtoService userDtoService;
	
	@PutMapping
	public ResponseEntity<?> updateUser(@RequestBody UserContentDto userContentDto){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			UserEntity user = userDtoService.getUserFromUserContentDto(userContentDto);
			UserEntity updatedUser = userDao.updateUser(user, loggedUserId);
			UserContentDto updatedUserDto = userDtoService.getUserContentDtoFromUser(updatedUser);
			responseContent.put("result", updatedUserDto);
			HttpStatus httpStatus = HttpStatus.CREATED;
			return httpStatus;
		});
	}
	
	@PutMapping("credentials")
	public ResponseEntity<?> updateCredentials(@RequestBody CredentialsDto credentials){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			UserEntity updatedUser = userDao.updateCredentials(loggedUserId, credentials.email(), credentials.password());
			responseContent.put("result", "Su nuevo correo es " + updatedUser.getEmail() + " y se ha cambiado la contraseña con éxito");
			HttpStatus httpStatus = HttpStatus.CREATED;
			return httpStatus;
		});
	}
	
	//PRUEBAS
	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			UserEntity user = userDao.getUserById(id);
			UserProfileResponseDto userDto = userDtoService.getUserProfileResponseDtoFromUser(user, loggedUserId);
			responseContent.put("result", userDto);
			HttpStatus httpStatus = HttpStatus.CREATED;
			return httpStatus;
		});
	}
	
	@GetMapping("creator/forum/{id}")
	public ResponseEntity<?> getForumCreatorByForumId(@PathVariable Long id){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			UserEntity creator = forumInteractionDao.getForumCreator(id);
			UserProfileResponseDto creatorDto = userDtoService.getUserProfileResponseDtoFromUser(creator, loggedUserId);
			responseContent.put("result", creatorDto);
			HttpStatus httpStatus = HttpStatus.CREATED;
			return httpStatus;
		});
	}
	
	@GetMapping("/nick/{nick}")
	public ResponseEntity<?> getUserByNick(@PathVariable String nick){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			UserEntity user = userDao.getUserByNick(nick);
			UserProfileResponseDto userDto = userDtoService.getUserProfileResponseDtoFromUser(user, loggedUserId);
			responseContent.put("result", userDto);
			HttpStatus httpStatus = HttpStatus.CREATED;
			return httpStatus;
		});
	}
	
	@PostMapping("/follow/{id}")
	public ResponseEntity<?> followUser(@PathVariable Long id){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Follow follow = followDao.followUser(id, loggedUserId);
			UserEntity followedUser = follow.getFollowed();
			responseContent.put("result", "El usuario logeado ha comenzado a seguir al usuario " 
					+ followedUser.getNick()
					+ " con id " + followedUser.getId());
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@PostMapping("/unfollow/{id}")
	public ResponseEntity<?> unfollowUser(@PathVariable Long id){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			UserEntity unfollowedUser = followDao.unfollowUser(id, loggedUserId);
			responseContent.put("result", "El usuario logeado ha dejado de seguir al usuario " 
					+ unfollowedUser.getNick()
					+ " con id " + unfollowedUser.getId());
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping("/forum/{forumId}")
	public ResponseEntity<?> getForumUsers(@PathVariable Long forumId, Pageable pageable){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<UserEntity> forumUsers = forumInteractionDao.getForumUsersByForumId(forumId, pageable);
			Page<UserForumResponseDto> forumUsersDto = userDtoService.getUserForumResponseDtosFromUsers(forumUsers, forumId, loggedUserId);
			responseContent.put("result", forumUsersDto);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping("/forum/{forumId}/nick/{nick}")
	public ResponseEntity<?> getForumUsers(@PathVariable Long forumId,@PathVariable String nick, Pageable pageable){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<UserEntity> forumUsers = forumInteractionDao.getForumUsersByForumIdAndNick(forumId, nick, pageable);
			Page<UserForumResponseDto> forumUsersDto = userDtoService.getUserForumResponseDtosFromUsers(forumUsers, forumId, loggedUserId);
			responseContent.put("result", forumUsersDto);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping("/forum/{forumId}/bannedUsers")
	public ResponseEntity<?> getBannedUsers(@PathVariable Long forumId, Pageable pageable){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<UserEntity> bannedUsers = forumInteractionDao.getBannedUsersByForumId(forumId, pageable);
			Page<UserForumResponseDto> forumUsersDto = userDtoService.getUserForumResponseDtosFromUsers(bannedUsers, forumId, loggedUserId);
			responseContent.put("result", forumUsersDto);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping("/forum/{forumId}/bannedUsers/{nick}")
	public ResponseEntity<?> getBannedUsers(@PathVariable Long forumId, @PathVariable String nick, Pageable pageable){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<UserEntity> bannedUsers = forumInteractionDao.getBannedUsersByForumIdAndNick(forumId, nick, pageable);
			Page<UserForumResponseDto> forumUsersDto = userDtoService.getUserForumResponseDtosFromUsers(bannedUsers, forumId, loggedUserId);
			responseContent.put("result", forumUsersDto);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping("/thread/{postId}")
	public ResponseEntity<?> getThreadUsers(@PathVariable Long postId, Pageable pageable){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<UserEntity> threadUsers = postInteractionDao.getThreadUsersByPostId(postId, pageable);
			Page<UserForumResponseDto> threadUsersDto = userDtoService.getThreadUserForumResponseDtosFromUsers(threadUsers, postId, loggedUserId);
			responseContent.put("result", threadUsersDto);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping("/thread/{postId}/nick/{nick}")
	public ResponseEntity<?> getThreadUsersByNick(@PathVariable Long postId, @PathVariable String nick, Pageable pageable){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<UserEntity> threadUsers = postInteractionDao.getThreadUsersByPostIdAndNick(postId, nick, pageable);
			Page<UserForumResponseDto> threadUsersDto = userDtoService.getThreadUserForumResponseDtosFromUsers(threadUsers, postId, loggedUserId);
			responseContent.put("result", threadUsersDto);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping("/followers")
	public ResponseEntity<?> getFollowers(Pageable pageable){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<UserEntity> followers = followDao.getFollowersByLogedUserId(loggedUserId, pageable);
			Page<UserProfileResponseDto> followerDtos = userDtoService.getUserProfileResponseDtosFromUser(followers, loggedUserId);
			responseContent.put("result", followerDtos);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping("/followers/{nick}")
	public ResponseEntity<?> getFollowersByNick(@PathVariable String nick, Pageable pageable){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<UserEntity> followers = followDao.getFollowersByLogedUserIdAndNick(loggedUserId, nick, pageable);
			Page<UserProfileResponseDto> followerDtos = userDtoService.getUserProfileResponseDtosFromUser(followers, loggedUserId);
			responseContent.put("result", followerDtos);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping("/followedUsers")
	public ResponseEntity<?> getFollows(Pageable pageable){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<UserEntity> followedUsers = followDao.getFollowedUsersByLogedUserId(loggedUserId, pageable);
			Page<UserProfileResponseDto> followedUserDtos = userDtoService.getUserProfileResponseDtosFromUser(followedUsers, loggedUserId);
			responseContent.put("result", followedUserDtos);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
	
	@GetMapping("/followedUsers/{nick}")
	public ResponseEntity<?> getFollowsByNick(@PathVariable String nick, Pageable pageable){
		return responseHandler.handleResponse((responseContent, loggedUserId) -> {
			Page<UserEntity> followedUsers = followDao.getFollowedUsersByLogedUserIdAndNick(loggedUserId, nick, pageable);
			Page<UserProfileResponseDto> followedUserDtos = userDtoService.getUserProfileResponseDtosFromUser(followedUsers, loggedUserId);
			responseContent.put("result", followedUserDtos);
			HttpStatus httpStatus = HttpStatus.OK;
			return httpStatus;
		});
	}
}
