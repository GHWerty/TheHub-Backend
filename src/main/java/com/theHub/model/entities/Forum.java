package com.theHub.model.entities;

import java.sql.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="forums")
public class Forum {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 50)
	private String title;
	
	@Column(nullable = false, length = 50)
	private String status;
	
	@Column(nullable = false, length = 250)
	private String description;
	
	@Column(nullable = false, length = 500)
	private String rules;
	
	@CreationTimestamp
	@Column(nullable = false)
	private Date creationDate;
	
	@Column(nullable = false, columnDefinition = "INT DEFAULT 0")
	private int interactions;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="creator_id", nullable = false)
	private UserEntity creator;
	
	//RELACIONES
	@OneToMany(mappedBy = "forum", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Post> posts;
	
	@OneToMany(mappedBy = "forum", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<ForumUser> forumUser;
	
}