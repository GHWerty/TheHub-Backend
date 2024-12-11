package com.theHub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.theHub.model.dto.forumDto.ForumContentDto;
import com.theHub.model.dto.forumDto.ForumProfileResponseDto;
import com.theHub.model.dto.forumDto.ForumResponseDto;
import com.theHub.model.dto.forumDto.ForumUpdateDto;
import com.theHub.model.entities.Forum;

@Mapper(componentModel = "spring")
public interface ForumMapper {

    @Mapping(source = "title", target = "title")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "rules", target = "rules")
    Forum forumContentDtoToForum(ForumContentDto dto);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "rules", target = "rules")
    Forum forumUpdateDtoToForum(ForumUpdateDto dto);
    
    @Mapping(source = "id", target = "id")
	@Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "rules", target = "rules")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "creator.nick", target = "creator")
    ForumProfileResponseDto forumToForumProfileResponseDto(Forum forum);
    
    @Mapping(source = "title", target = "title")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "rules", target = "rules")
    ForumContentDto forumToForumContentDto(Forum forum);
	
	@Mapping(source = "id", target = "id")
	@Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "rules", target = "rules")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "creator.nick", target = "creator")
    ForumResponseDto forumToForumResponseDto(Forum forum);
	
}