package com.theHub.model.dto.forumDto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumResponseDto {
	
	private Long id;
	private Long creatorId;
	private String title;
	private String status;
	private String description;
	private String rules;
	private Date creationDate;
	private String creator;
	boolean isBanned;
	boolean isFollowed;
	boolean isAdmin;
	
}