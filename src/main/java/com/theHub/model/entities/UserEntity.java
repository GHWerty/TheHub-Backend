package com.theHub.model.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique=true, length = 20, nullable = false)
	private String nick;
	
	@Column(unique=true, nullable = false)
	private String email;
	
	@Column(length = 50)
	private String status;
	
	@Column(length = 250)
	private String biography;
	
	@Column(nullable = false)
	private String password;
	
	//CREADEOR DE FOROS
	@OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
	private List<Forum> forums;
	
	//AUTOR DE POSTS
	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
	private List<Post> posts;

}