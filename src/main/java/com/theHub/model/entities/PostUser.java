package com.theHub.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="post_user")
public class PostUser {
	
	@EmbeddedId
	private PostUserPk postUserPk;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("postId")
	@JoinColumn(name="post_id")
	private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	@JoinColumn(name="user_id")
	private UserEntity user;
	
	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT 0")
	private boolean liked;
	
	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT 0")
	private boolean saved; 

}