package com.theHub.model.persist.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.theHub.exception.AppException;
import com.theHub.model.entities.Forum;
import com.theHub.model.entities.UserEntity;
import com.theHub.model.persist.dao.ForumDao;
import com.theHub.model.persist.dao.UserDao;
import com.theHub.model.persist.repository.ForumRepository;

import jakarta.transaction.Transactional;

@Service
public class ForumDaoImpl implements ForumDao {

	@Autowired
	private ForumRepository forumRep;

	@Autowired
	private UserDao userDao;

	@Override
	@Transactional
	public Forum createForum(Forum forum, Long loggedUserId) {

		UserEntity logedUser = userDao.getUserById(loggedUserId);

		forum.setCreator(logedUser);
		forum.setInteractions(0);
		
		Forum createdForum = forumRep.save(forum);
		return createdForum;
	}

	@Override
	public Forum getForumById(Long forumId) {
		
		Forum savedForum = forumRep.findById(forumId)
				.orElseThrow(() -> new AppException("No se ha podido encontrar el foro", HttpStatus.NOT_FOUND));
		
		return savedForum;
	}

	@Override
	public Forum updateForum(Forum forum) {
		
		Forum savedForum = forumRep.findById(forum.getId())
				.orElseThrow(() -> new AppException("No se ha podido encontrar el foro", HttpStatus.NOT_FOUND));
		
		savedForum.setTitle(forum.getTitle());
		savedForum.setStatus(forum.getStatus());
		savedForum.setDescription(forum.getDescription());
		savedForum.setRules(forum.getRules());

		Forum updatedForum = forumRep.save(savedForum);
		return updatedForum;
	}

	// VERIFICAR ACCESO CON SECURITY -> ADMIN DEL FORO O ROLE ADMIN
	@Override
	public void deleteForumById(Long forumId) {
		
		if (!forumRep.existsById(forumId))
			throw new AppException("No se ha encontrado el foro", HttpStatus.NOT_FOUND);
		
		forumRep.deleteById(forumId);
	}

	@Override
	public Page<Forum> getForumsSortedAndPaginated(Pageable pageable) {
		Page<Forum> forums = forumRep.findAll(pageable);
		return forums;
	}

	@Override
	public Page<Forum> getForumsByTitle(String title, Pageable pageable) {
		Page<Forum> forums = forumRep.findByTitleContaining(title, pageable);
		return forums;
	}

}