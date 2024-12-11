package com.theHub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.theHub.model.dto.userDto.UserContentDto;
import com.theHub.model.dto.userDto.UserContentResponseDto;
import com.theHub.model.dto.userDto.UserForumResponseDto;
import com.theHub.model.dto.userDto.UserProfileResponseDto;
import com.theHub.model.entities.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "nick", target = "nick")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "biography", target = "biography")
    UserForumResponseDto userToUserForumResponseDto(UserEntity user);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nick", target = "nick")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "biography", target = "biography")
    UserContentResponseDto userToUserContentResponseDto(UserEntity user);

    @Mapping(source = "nick", target = "nick")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "biography", target = "biography")
    UserEntity userContentDtoToUser(UserContentDto userFormDto);
    
    @Mapping(source = "nick", target = "nick")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "biography", target = "biography")
    UserContentDto userToUserContentDto(UserEntity user);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nick", target = "nick")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "biography", target = "biography")
    UserProfileResponseDto userToUserProfileResponseDto(UserEntity user);
    
}
