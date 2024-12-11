package com.theHub.model.persist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.theHub.model.entities.Forum;
import com.theHub.model.entities.ForumUser;
import com.theHub.model.entities.ForumUserPk;
import com.theHub.model.entities.UserEntity;

@Repository
public interface ForumUserRepository extends JpaRepository<ForumUser, ForumUserPk>{

	 @Query("SELECT fu.user FROM ForumUser fu WHERE fu.forumUserPk.forumId = :forumId AND fu.banned = false")
	 Page<UserEntity> findUsersByForumId(@Param("forumId") Long forumId, Pageable pageable);
	 
	 @Query("SELECT fu.user FROM ForumUser fu WHERE fu.forumUserPk.forumId = :forumId AND fu.user.nick LIKE %:nick% AND fu.banned = false")
	 Page<UserEntity> findUsersByForumIdAndNickContaining(@Param("forumId") Long forumId, @Param("nick") String nick, Pageable pageable);
	 
	 @Query("SELECT fu.user FROM ForumUser fu WHERE fu.forumUserPk.forumId = :forumId AND fu.banned = true")
	 Page<UserEntity> findBanedUsersByForumId(@Param("forumId") Long forumId, Pageable pageable);
	 
	 @Query("SELECT fu.user FROM ForumUser fu WHERE fu.forumUserPk.forumId = :forumId AND fu.user.nick LIKE %:nick% AND fu.banned = true")
	 Page<UserEntity> findBannedUsersByForumIdAndNickContaining(@Param("forumId") Long forumId, @Param("nick") String nick, Pageable pageable);
	 
	 @Query("SELECT fu.forum FROM ForumUser fu JOIN fu.forum f WHERE fu.forumUserPk.userId = :userId AND fu.followed = true ORDER BY f.interactions")
	 Page<Forum> findForumsByUserId(@Param("userId") Long userId, Pageable pageable);
	 
	 @Query("SELECT fu.forum FROM ForumUser fu JOIN fu.forum f WHERE fu.forumUserPk.userId = :userId AND fu.followed = true AND f.title LIKE %:title% ORDER BY f.interactions")
	 Page<Forum> findForumsByUserIdAndTitleContaining(@Param("userId") Long userId, @Param("title") String title, Pageable pageable);
	 
	 @Query("SELECT f FROM Forum f WHERE f.creator.id = :userId")
	 Page<Forum> findForumsByCreatorId(@Param("userId") Long userId, Pageable pageable);
	 
	 @Query("SELECT f FROM Forum f WHERE f.creator.id = :userId AND f.title LIKE %:title% ORDER BY f.interactions")
	 Page<Forum> findForumsByCreatorIdAndTitleContaining(@Param("userId") Long userId, @Param("title") String title, Pageable pageable);
	 
	 @Query("SELECT f.creator FROM Forum f WHERE f.id = :forumId")
	 UserEntity findCreatorByForumId(Long forumId);
	 
}