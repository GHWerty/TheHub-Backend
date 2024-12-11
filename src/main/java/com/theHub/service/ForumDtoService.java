package com.theHub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.theHub.mapper.ForumMapper;
import com.theHub.model.dto.forumDto.ForumContentDto;
import com.theHub.model.dto.forumDto.ForumProfileResponseDto;
import com.theHub.model.dto.forumDto.ForumResponseDto;
import com.theHub.model.dto.forumDto.ForumUpdateDto;
import com.theHub.model.entities.Forum;
import com.theHub.model.entities.ForumUser;
import com.theHub.model.entities.ForumUserPk;
import com.theHub.model.persist.dao.ForumInteractionDao;

@Service
public class ForumDtoService {
	
	@Autowired
	private ForumMapper forumMapper;
	
	@Autowired
	private ForumInteractionDao forumInteractionDao;
	
	//LISTA LOS FOROS Y AÑADIR NUESTRA INTERACCIÓN
	public ForumResponseDto getForumResponseDtoFromForum(Forum forum, Long loggeedUserId) {
		
		ForumResponseDto dto = forumMapper.forumToForumResponseDto(forum);
		
		ForumUserPk relationPk = new ForumUserPk(forum.getId(), loggeedUserId);
		ForumUser relation = forumInteractionDao.getForumRelationById(relationPk);
		if (relation == null) {
			dto.setAdmin(false);
			dto.setFollowed(false);
			dto.setBanned(false);
		} else {
			if(relation.isBanned()) {
				dto.setAdmin(false);
				dto.setFollowed(relation.isBanned());
				dto.setBanned(true);
			} else {
				dto.setAdmin(relation.isAdmin());
				dto.setFollowed(true);
				dto.setBanned(false);
			}
		}
		return dto;
	}
	
	public Page<ForumResponseDto> getForumResponseDtosFromForums(Page<Forum> forums, Long loggeedUserId) {
		
		Page<ForumResponseDto> forumDtos = forums.map(forum ->  getForumResponseDtoFromForum(forum, loggeedUserId));
		return forumDtos;
	}
	
	public Forum getForumFromForumContentDto(ForumContentDto forumContentDto) {
		Forum forum = forumMapper.forumContentDtoToForum(forumContentDto);
		return forum;
	}
	
	public Forum getForumFromForumUpdateDto(ForumUpdateDto forumUpdateDto) {
		Forum forum = forumMapper.forumUpdateDtoToForum(forumUpdateDto);
		return forum;
	}
	
	public ForumProfileResponseDto getForumProfileResponseDtoFromForum(Forum forum) {
		ForumProfileResponseDto forumDto = forumMapper.forumToForumProfileResponseDto(forum);
		return forumDto;
	}
	
	public Page<ForumProfileResponseDto> getForumProfileResponseDtosFromForums (Page<Forum> forums) {
		Page<ForumProfileResponseDto> forumDtos = forums.map(forum -> getForumProfileResponseDtoFromForum(forum));
		return forumDtos;
	}

}