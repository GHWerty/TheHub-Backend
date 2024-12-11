package com.theHub.model.persist.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.theHub.exception.AppException;
import com.theHub.model.entities.Forum;
import com.theHub.model.entities.Post;
import com.theHub.model.entities.PostUser;
import com.theHub.model.entities.PostUserPk;
import com.theHub.model.entities.UserEntity;
import com.theHub.model.persist.dao.PostDao;
import com.theHub.model.persist.dao.PostInteractionDao;
import com.theHub.model.persist.dao.UserDao;
import com.theHub.model.persist.repository.PostUserRepository;

import jakarta.transaction.Transactional;

@Service
public class PostInteractionDaoImpl implements PostInteractionDao{

	@Autowired
	private UserDao userDao;

	@Autowired
	private PostDao postDao;

	@Autowired
	private PostUserRepository postUserRep;
	

	@Override
	public PostUser getPostRelationById(PostUserPk postUserPk) {
		return postUserRep.findById(postUserPk).orElse(null);
	}

	// CREA LA RELACIÓN SIN PERSISTIRLA PARA RESPETAR EL LIKE O EL GUARDADO
	private PostUser createInteraction(PostUserPk interactionPk) {

		Post post = postDao.getPostById(interactionPk.getPostId());
		UserEntity loggedUser = userDao.getUserById(interactionPk.getUserId());

		PostUser interaction = PostUser.builder()
				.postUserPk(interactionPk)
				.post(post)
				.user(loggedUser)
				.liked(false)
				.saved(false)
				.build();
		return interaction;
	}

	@Override
	@Transactional
	public Post interactionHandler(Long postId, Long loggedUserId, String action) {

		Post post = postDao.getPostById(postId);
		Forum forum = post.getForum();
		
		PostUserPk interactionPk = new PostUserPk(postId, loggedUserId);
		PostUser interaction = postUserRep.findById(interactionPk).orElse(null);
		
		if (("UNLIKE".equalsIgnoreCase(action) || "UNSAVE".equalsIgnoreCase(action)) && interaction == null)
			throw new AppException("El usuario no interactuó con el post anteriormente", HttpStatus.FORBIDDEN);
		else if (interaction == null)
			interaction = createInteraction(interactionPk);
		
		switch (action) {
		
			case "LIKE":
				if (interaction.isLiked())
					throw new AppException("El usuario ya ha dado like a este post", HttpStatus.FORBIDDEN);
				interaction.setLiked(true);
				post.setLikes(post.getLikes() + 1);
				forum.setInteractions(forum.getInteractions() + 1);
				break;
				
			case "UNLIKE":
				if (!interaction.isLiked())
					throw new AppException("El usuario no dio like al post con anterioridad", HttpStatus.FORBIDDEN);
				interaction.setLiked(false);
				post.setLikes(post.getLikes() - 1);
				forum.setInteractions(forum.getInteractions() - 1);
				break;
				
			case "SAVE":
				if (interaction.isSaved())
					throw new AppException("El usuario ya tiene guardado este post", HttpStatus.FORBIDDEN);
				interaction.setSaved(true);
				post.setSaveds(post.getSaveds() + 1);
				forum.setInteractions(forum.getInteractions() + 1);
				break;
				
			case "UNSAVE":
				if(!interaction.isSaved())
					throw new AppException("El usuario no guardó el post con anterioridad", HttpStatus.FORBIDDEN);
				interaction.setSaved(false);
				post.setSaveds(post.getSaveds() - 1);
				forum.setInteractions(forum.getInteractions() - 1);
				break;
				
			default:
				throw new AppException("Acción no válida", HttpStatus.BAD_REQUEST);
		}
		
		if (!interaction.isLiked() && !interaction.isSaved())
			postUserRep.deleteById(interactionPk);
		else
			postUserRep.save(interaction);
		
		return post;
	}
	
	@Override
	public Page<Post> getPostsSavedByUserId(Long loggedUserId, Pageable pageable) {
		
		Page<Post> savedPosts = postUserRep.findSavedPostsByUserId(loggedUserId, pageable);
		return savedPosts;
	}

	@Override
	public Page<UserEntity> getThreadUsersByPostId(Long postId, Pageable pageable) {
		Page<UserEntity> threadUsers = postUserRep.findThreadUsersByPostId(postId, pageable);
		if (threadUsers == null || threadUsers.isEmpty())
			//Siempre devolverá usuarios, como mínimo el autor del post padre
			throw new AppException("No se han podido recuperar los participantes", HttpStatus.INTERNAL_SERVER_ERROR);
		return threadUsers;
	}
	
	@Override
	public Page<UserEntity> getThreadUsersByPostIdAndNick(Long postId, String nick, Pageable pageable) {
		
		Page<UserEntity> threadUsers = postUserRep.findThreadUsersByPostIdAndNickContainer(postId, nick, pageable);
		return threadUsers;
	}

	@Override
	public Page<Post> getProfilePosts(Long loggedUserId, Pageable pageable) {
		Page<Post> profilePosts = postUserRep.findProfilePosts(loggedUserId, pageable);
		return profilePosts;
	}

}