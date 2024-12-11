package com.theHub.model.persist.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.theHub.exception.AppException;
import com.theHub.model.entities.Forum;
import com.theHub.model.entities.ForumUser;
import com.theHub.model.entities.ForumUserPk;
import com.theHub.model.entities.Post;
import com.theHub.model.entities.UserEntity;
import com.theHub.model.persist.dao.ForumInteractionDao;
import com.theHub.model.persist.dao.UserDao;
import com.theHub.model.persist.repository.ForumUserRepository;
import com.theHub.model.persist.repository.PostRepository;

import jakarta.transaction.Transactional;

@Service
public class ForumInteractionDaoImpl implements ForumInteractionDao{
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ForumDaoImpl forumDao;
	
	@Autowired
	private ForumUserRepository forumUserRep;
	
	@Autowired
	private PostRepository postRep;


	@Override
	public ForumUser getForumRelationById(ForumUserPk forumUserPk) {
		
		ForumUser relation = forumUserRep.findById(forumUserPk).orElse(null);
		return relation;
	}
	
	//SIEMPRE DEBE SEGUIR AL FORO, TANTO PARA SER ADMIN COMO CREADOR
	@Override
	public String followForum(Long forumId, Long loggedUserId) {
		ForumUserPk followPk = new ForumUserPk(forumId, loggedUserId);
		ForumUser forumRelation = getForumRelationById(followPk);
		
		if(forumRelation != null) {
			if(forumRelation.isFollowed())
				throw new AppException("El usuario ya sigue al foro", HttpStatus.BAD_REQUEST);
			else
				forumRelation.setFollowed(true);
		} else {
			UserEntity loggedUser = userDao.getUserById(loggedUserId);
			Forum followedForum = forumDao.getForumById(forumId);
			
			forumRelation = ForumUser.builder()
					.forumUserPk(followPk)
					.forum(followedForum)
					.user(loggedUser)
					.admin(false)
					.banned(false)
					.followed(true)
					.build();
		}
			
		forumUserRep.save(forumRelation);
		return "Has comenzado a seguir al foro " + forumRelation.getForum().getTitle();
	}
	
	@Override
	public String unfollowForum(Long forumId, Long loggedUserId) {
		ForumUserPk followPk = new ForumUserPk(forumId, loggedUserId);
		ForumUser forumRelation = getForumRelationById(followPk);
		
		if((forumRelation != null && !forumRelation.isFollowed()) || forumRelation == null)
			throw new AppException("No se pudo dejar de seguir al foro porque el usuario no lo seguía previamente", HttpStatus.FORBIDDEN);
		
		if(!forumRelation.isBanned())
			forumUserRep.deleteById(followPk);
		else {
			forumRelation.setFollowed(false);
			forumUserRep.save(forumRelation);
		}
		
		return "Has dejado de seguir al foro " + forumRelation.getForum().getTitle();
	}

	@Override
	public String updateAdminRole(Long loggedUserId, Long forumId, Long userId, boolean grantAdmin) {
		
		ForumUserPk userForumRelationPk = new ForumUserPk(forumId, userId);
		ForumUser userForumRelation = forumUserRep.findById(userForumRelationPk).orElse(null);
		
		if(userForumRelation == null || !userForumRelation.isFollowed())
				throw new AppException("El usuario no puede ser admin porque no sigue al foro", HttpStatus.FORBIDDEN);
		
		//Solo el propietario del foro puede dar y quitar el rol de administrador
		if (userForumRelation.getForum().getCreator().getId() != loggedUserId)
			 throw new AppException("El usuario logueado no tiene privilegios", HttpStatus.FORBIDDEN);
		
		if (userForumRelation.isAdmin() && grantAdmin)
			throw new AppException("El usuario ya es admin del foro", HttpStatus.BAD_REQUEST);
		else if (!userForumRelation.isAdmin() && !grantAdmin)
			throw new AppException("El usuario no es admin del foro", HttpStatus.BAD_REQUEST);
		
		userForumRelation.setAdmin(grantAdmin);
		forumUserRep.save(userForumRelation);
		
		
		
		return grantAdmin ? "El usuario ahora es admin del foro " + userForumRelation.getForum().getTitle() :
			"El usuario ha dejado de ser administrador del foro " + userForumRelation.getForum().getTitle();
	}
	
	@Override
	@Transactional
	public String updateBan(Long loggedUserId, Long forumId, Long userId, boolean banned) {
		
		String result = "";
		
		ForumUserPk userForumRelationPk = new ForumUserPk(forumId, userId);
		ForumUser userForumRelation = forumUserRep.findById(userForumRelationPk).orElse(null);
		
		ForumUserPk loggedUserForumRelationPk = new ForumUserPk(forumId, loggedUserId);
		ForumUser loggedUserForumRelation = forumUserRep.findById(loggedUserForumRelationPk).orElse(null);
		
		UserEntity bannedUser = userDao.getUserById(userId);
		Forum bannedForum = forumDao.getForumById(forumId);
		
		if ((loggedUserForumRelation != null && !loggedUserForumRelation.isAdmin()) ||
				(loggedUserForumRelation == null && bannedForum.getCreator().getId() != loggedUserId))
			 throw new AppException("El usuario logueado no tiene privilegios", HttpStatus.FORBIDDEN);
		
		if (userForumRelation != null) {
			if (userForumRelation.isBanned() && banned)
				throw new AppException("El usuario ya ha sido baneado", HttpStatus.BAD_REQUEST);
			else if (!userForumRelation.isBanned() && !banned)
				throw new AppException("El usuario no ha sido baneado con anterioridad", HttpStatus.BAD_REQUEST);
			else if (!userForumRelation.isBanned() && banned) {
				
				if (userForumRelation.isAdmin() && bannedForum.getCreator().getId() != loggedUserId)
					throw new AppException("Solo el propietario del foro puede banear administradores", HttpStatus.FORBIDDEN);
				
				userForumRelation.setAdmin(false);
				userForumRelation.setBanned(true);
				forumUserRep.save(userForumRelation);
				result = "El usuario ha sido baneado con éxito";
			} else if (userForumRelation.isBanned() && !banned) {
				if(!userForumRelation.isFollowed())
					forumUserRep.deleteById(userForumRelationPk);
				else {
					userForumRelation.setBanned(false);
					forumUserRep.save(userForumRelation);
				}
				
				result = "El usuario ha sido desbaneado con éxito.Podrá volver a escribir mensajes en el foro";
			}
		} else { //El usuario tiene publicaciones en un foro pero no es seguidor a la hora de banearlo
			ForumUser bannedRelation = ForumUser.builder()
					.forumUserPk(userForumRelationPk)
					.forum(bannedForum)
					.user(bannedUser)
					.admin(false)
					.banned(true)
					.followed(false)
					.build();
			forumUserRep.save(bannedRelation);
			
			result = "El usuario ha sido baneado con éxito";
		}
			
		if(banned) {
			List<Post> bannedUserPosts = bannedUser.getPosts().stream().filter(p -> p.getForum().getId() == forumId).collect(Collectors.toList());
			postRep.deleteAll(bannedUserPosts);
		}
		
		return result;
	}

	@Override
	public Page<UserEntity> getForumUsersByForumId(Long forumId, Pageable pageable) {
		
		Page<UserEntity> forumUsers = forumUserRep.findUsersByForumId(forumId, pageable);
		
		Forum forum = forumDao.getForumById(forumId);
		UserEntity creator = forum.getCreator();
		
		if(creator == null)
			throw new AppException("No se ha podido recuperar el creador del foro", HttpStatus.NOT_FOUND);
		
		List<UserEntity> forumUsersMutableList = new ArrayList<UserEntity>(forumUsers.getContent());
		forumUsersMutableList.add(0, forum.getCreator());
		
		Page<UserEntity> forumUsersAndCreator = new PageImpl<UserEntity>(forumUsersMutableList, forumUsers.getPageable(), forumUsers.getTotalElements() + 1); 
		return forumUsersAndCreator;
	}
	
	@Override
	public Page<UserEntity> getForumUsersByForumIdAndNick(Long forumId, String nick, Pageable pageable) {
		
		Page<UserEntity> forumUsers = forumUserRep.findUsersByForumIdAndNickContaining(forumId, nick, pageable);
		
		Forum forum = forumDao.getForumById(forumId);
		UserEntity creator = forum.getCreator();
		
		long totalElements = forumUsers.getTotalElements();
		
		if(creator == null)
			throw new AppException("No se ha podido recuperar el creador del foro", HttpStatus.NOT_FOUND);
		
		List<UserEntity> forumUsersMutableList = new ArrayList<UserEntity>(forumUsers.getContent());
		if(creator.getNick().toLowerCase().contains(nick.toLowerCase())) {
			forumUsersMutableList.add(0, forum.getCreator());
			totalElements++;
		}
			
		
		Page<UserEntity> forumUsersAndCreator = new PageImpl<UserEntity>(forumUsersMutableList, forumUsers.getPageable(), totalElements); 
		return forumUsersAndCreator;
	}
	
	@Override
	public Page<UserEntity> getBannedUsersByForumId(Long forumId, Pageable pageable) {
		Page<UserEntity> bannedUsers = forumUserRep.findBanedUsersByForumId(forumId, pageable);
		return bannedUsers;
	}

	@Override
	public Page<UserEntity> getBannedUsersByForumIdAndNick(Long forumId, String nick, Pageable pageable) {
		Page<UserEntity> bannedUsers = forumUserRep.findBannedUsersByForumIdAndNickContaining(forumId, nick, pageable);
		return bannedUsers;
	}

	@Override
	public Page<Forum> getFollowedForumsByUser(Long userId, Pageable pageable) {
		Page<Forum> followedForums = forumUserRep.findForumsByUserId(userId, pageable);
		return followedForums;
	}
	
	@Override
	public Page<Forum> getFollowedForumsByUserIdAndTitle(Long userId, String title, Pageable pageable) {
		
		Page<Forum> followedForums = forumUserRep.findForumsByUserIdAndTitleContaining(userId, title, pageable);
		return followedForums;
	}

	@Override
	public Page<Forum> getForumsByCreatorId(Long userId, Pageable pageable) {
		
		Page<Forum> createdForums = forumUserRep.findForumsByCreatorId(userId, pageable);
		return createdForums;
	}

	@Override
	public UserEntity getForumCreator(Long forumId) {
		
		UserEntity creator = forumUserRep.findCreatorByForumId(forumId);
		return creator;
	}

	@Override
	public Page<Forum> getForumsByCreatorIdAndTitle(Long userId, String title, Pageable pageable) {
		Page<Forum> createdForums = forumUserRep.findForumsByCreatorIdAndTitleContaining(userId, title, pageable);
		return createdForums;
	}
	

}
