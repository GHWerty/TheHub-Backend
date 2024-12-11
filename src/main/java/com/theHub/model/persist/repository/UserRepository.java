package com.theHub.model.persist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.theHub.model.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{

	UserEntity findByNick(String nick);
	UserEntity findByEmail(String email);
	boolean existsByNick(String nick);
	boolean existsByEmail(String email);
}
