package com.theHub.model.dto.forumDto;

import java.sql.Date;

public record ForumProfileResponseDto(
		
		Long id,
		Long creatorId,
		String title, 
		String status, 
		String description, 
		String rules,
		String creator,
		Date creationDate
) { }