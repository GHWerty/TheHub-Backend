package com.theHub.model.persist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.theHub.model.entities.Post;
import com.theHub.model.entities.PostUser;
import com.theHub.model.entities.PostUserPk;
import com.theHub.model.entities.UserEntity;

@Repository
public interface PostUserRepository extends JpaRepository<PostUser, PostUserPk> {

	@Query("SELECT pu.post FROM PostUser pu WHERE pu.user.id = :userId AND pu.saved = true")
	Page<Post> findSavedPostsByUserId(@Param("userId") Long userId, Pageable pageable);

	@Query("SELECT p.author FROM Post p WHERE p.id = :postId OR p.repliedPost.id = :postId")
	Page<UserEntity> findThreadUsersByPostId(@Param("postId") Long postId, Pageable pageable);
	
	@Query("SELECT p.author FROM Post p WHERE p.author.nick LIKE %:nick% AND (p.id = :postId OR p.repliedPost.id = :postId)")
	Page<UserEntity> findThreadUsersByPostIdAndNickContainer(@Param("postId") Long postId,@Param("nick") String nick, Pageable pageable);

	@Query("SELECT p FROM Post p " +
		       "WHERE (p.forum.id IN (" +
		       "   SELECT fu.forum.id FROM ForumUser fu " +
		       "   WHERE fu.user.id = :userId AND fu.followed = true OR fu.forum.creator.id = :userId" +
		       ") " +
		       "OR p.author.id = :userId " +
		       "OR p.author.id IN (" +
		       "   SELECT f.followPk.followedId FROM Follow f " +
		       "   WHERE f.followPk.followerId = :userId" +
		       ") ) " +
		       "ORDER BY p.postDate DESC")
	Page<Post> findProfilePosts(@Param("userId") Long userId, Pageable pageable);
}