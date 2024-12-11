package com.theHub.model.persist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.theHub.model.entities.Forum;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Long>{
	
	Page<Forum> findByTitleContaining(String title, Pageable pageable);
	
}