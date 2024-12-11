package com.theHub.model.persist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.theHub.model.entities.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

	// RECUPERAR POSTS DE UN FORO CON PAGINACIÓN
    @Query("SELECT p FROM Post p WHERE p.forum.id = :forumId AND p.repliedPost IS NULL")
    Page<Post> findPostsByForumId(@Param("forumId") Long forumId, Pageable pageable);

    // RECUPERAR RESPUESTAS DE UN POST CON PAGINACIÓN
    @Query("SELECT p FROM Post p WHERE p.repliedPost.id = :repliedPostId")
    Page<Post> findPostsByRepliedPostId(@Param("repliedPostId") Long repliedPostId, Pageable pageable);
    
    //POSTS DE UN USUARIO
    @Query("SELECT p FROM Post p " +
    	       "WHERE p.author.id = :authorId " +
    	       "ORDER BY p.postDate DESC")
    Page<Post> findPostsByUserId(@Param("authorId") Long authorId, Pageable pageable);

	@Query("SELECT COUNT(p) > 0 FROM Post p WHERE p.repliedPost.id = :postId")
	boolean hasReplies(@Param("postId") Long postId);
		
}