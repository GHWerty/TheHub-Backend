package com.theHub.model.dto.forumDto;

public record ForumUpdateDto(
		
		Long id,
		String title, 
		String status, 
		String description, 
		String rules

) { }