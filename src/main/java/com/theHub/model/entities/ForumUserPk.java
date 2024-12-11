package com.theHub.model.entities;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ForumUserPk implements Serializable{

	private Long forumId;
	private Long userId;
	
}