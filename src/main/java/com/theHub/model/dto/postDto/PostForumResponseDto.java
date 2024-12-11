package com.theHub.model.dto.postDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostForumResponseDto {

	private Long postId;
	private Long forumId;
	private Long authorId;
	private Long repliedPostId;
	private String forumName;
	private String title;
	private String text;
	private String author;
	private String elapsedTime;
	private boolean hasPermissons;
	private boolean isBanned;
	private boolean isReplied;
	private int likes;
	private int saveds;
	private boolean isLiked;
	private boolean isSaved;
	
}