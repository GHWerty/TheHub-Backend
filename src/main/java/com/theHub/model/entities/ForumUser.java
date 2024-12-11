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
@Table(name="forum_user")
public class ForumUser {

	@EmbeddedId
	private ForumUserPk forumUserPk;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("forumId")
	@JoinColumn(name="forum_id")
	private Forum forum;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	@JoinColumn(name="user_id")
	private UserEntity user;

	@Column(name = "is_admin", columnDefinition = "BOOLEAN DEFAULT 0", nullable = false)
	private boolean admin;
	
	@Column(name = "is_baned", columnDefinition = "BOOLEAN DEFAULT 0", nullable = false)
	private boolean banned;
	
	@Column(name = "is_followed", columnDefinition = "BOOLEAN DEFAULT 0", nullable = false)
	private boolean followed;
	
}