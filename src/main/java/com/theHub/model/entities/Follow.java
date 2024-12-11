package com.theHub.model.entities;

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
@Table(name="followers")
public class Follow {

	@EmbeddedId
	private FollowPk followPk;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("followedId")
	@JoinColumn(name="followed_id")
	private UserEntity followed;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("followerId")
	@JoinColumn(name="follower_id")
	private UserEntity follower;
	
}