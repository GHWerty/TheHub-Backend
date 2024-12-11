package com.theHub.model.entities;

import java.time.Instant;
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
@Table(name="posts")
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 60)
	private String title;
	
	@CreationTimestamp
	@Column(nullable = false)
	private Instant postDate;
	
	@Column(nullable = false, length = 500)
	private String text;
	
	@Column(nullable = false, columnDefinition = "INT DEFAULT 0")
	private int likes;
	
	@Column(nullable = false, columnDefinition = "INT DEFAULT 0")
	private int saveds;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id", nullable = false)
	private UserEntity author;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="forum_id", nullable = false)
	private Forum forum;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="replied_post_id")
	private Post repliedPost;
	
	//INTERACCIONES CON LOS USUARIOS. SE PUEDE VER QUIENES LES HAN DADO LIKE
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<PostUser> postUser;
	
	@OneToMany(mappedBy = "repliedPost", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Post> replies;

}