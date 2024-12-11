package com.theHub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.theHub.model.dto.postDto.PostContentDto;
import com.theHub.model.dto.postDto.PostForumResponseDto;
import com.theHub.model.dto.postDto.PostThreadResponseDto;
import com.theHub.model.dto.postDto.PostUpdateDto;
import com.theHub.model.entities.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {
	
	@Mapping(source = "title", target = "title")
	@Mapping(source = "text", target = "text")
	Post postContentDtoToPost(PostContentDto dto);
	
	@Mapping(source = "id", target = "id")
	@Mapping(source = "title", target = "title")
	@Mapping(source = "text", target = "text")
	Post postUpdateDtoToPost(PostUpdateDto dto);

	@Mapping(source = "id", target = "postId")
	@Mapping(source = "repliedPost.id", target = "repliedPostId")
	@Mapping(source = "forum.id", target = "forumId")
	@Mapping(source = "forum.title", target = "forumName")
	@Mapping(source = "title", target = "title")
	@Mapping(source = "text", target = "text")
	@Mapping(source = "author.nick", target = "author")
	@Mapping(source = "author.id", target = "authorId")
	@Mapping(source = "likes", target = "likes")
	@Mapping(source = "saveds", target = "saveds")
	PostForumResponseDto postToPostForumResponseDto(Post post);
	
	@Mapping(source = "id", target = "postId")
	@Mapping(source = "repliedPost.id", target = "repliedPostId")
	@Mapping(source = "forum.id", target = "forumId")
	@Mapping(source = "forum.title", target = "forumName")
	@Mapping(source = "title", target = "title")
	@Mapping(source = "text", target = "text")
	@Mapping(source = "author.nick", target = "author")
	@Mapping(source = "author.id", target = "authorId")
	@Mapping(source = "likes", target = "likes")
	@Mapping(source = "saveds", target = "saveds")
	PostThreadResponseDto postToPostThreadResponseDto(Post post);

}