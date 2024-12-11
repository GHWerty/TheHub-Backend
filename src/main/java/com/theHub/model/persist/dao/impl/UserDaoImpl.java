package com.theHub.model.persist.dao.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.theHub.exception.AppException;
import com.theHub.exception.AppMultipleErrosException;
import com.theHub.model.entities.UserEntity;
import com.theHub.model.persist.dao.UserDao;
import com.theHub.model.persist.repository.UserRepository;

@Service
public class UserDaoImpl implements UserDao{

	@Autowired
	private UserRepository userRep;

	@Override
	public UserEntity getUserById(Long userId) {
		
		UserEntity savedUser = userRep.findById(userId)
				.orElseThrow(() -> new AppException("No se ha podido recuperar el usuario", HttpStatus.NOT_FOUND));
		return savedUser;
	}

	@Override
	public UserEntity updateUser(UserEntity user, Long loggedUserId) {			
		
		UserEntity loggedUser = userRep.findById(loggedUserId)
				.orElseThrow(() -> new AppException("No se ha podido recuperar el usuario", HttpStatus.NOT_FOUND));
		
		if (!user.getNick().equals(loggedUser.getNick()) && userRep.existsByNick(user.getNick()))
				throw new AppException("El nick solicitado se encuentra en uso", HttpStatus.BAD_REQUEST);
		
		loggedUser.setNick(user.getNick());
		loggedUser.setStatus(user.getStatus());
		loggedUser.setBiography(user.getBiography());
		
		UserEntity updatedLoggedUser = userRep.save(loggedUser);
		return updatedLoggedUser;
	}

	@Override
	public UserEntity getUserByNick(String nick) {
		
		UserEntity savedUser = userRep.findByNick(nick);
		return savedUser;
	}

	@Override
	public Long getIdByCredentials(String email, String password) {
		
		UserEntity user = userRep.findByEmail(email);
		if (user == null)
			throw new AppException("No existe ningún usuario con el correo introducido", HttpStatus.NOT_FOUND);
		
		if (!user.getPassword().equals(password))
			throw new AppException("Contraseña incorrecta", HttpStatus.BAD_REQUEST);
			
		return user.getId();
	}

	@Override
	public Long createUser(String nick, String email, String password) {
		
		HashMap<String, String> errors = new HashMap<String, String>();
		
		if(userRep.existsByNick(nick))
			errors.put("nick", "El nick introducido ya se encontraba en uso");
		
		if(userRep.existsByEmail(email))
			errors.put("email", "El email introducido ya se encontraba en uso");
	
		if (!errors.isEmpty())
			throw new AppMultipleErrosException(errors, HttpStatus.BAD_REQUEST);
		
		UserEntity newUser = new UserEntity();
		newUser.setNick(nick);
		newUser.setEmail(email);
		newUser.setPassword(password);
		
		UserEntity registereduser = userRep.save(newUser);
		return registereduser.getId();
	}

	@Override
	public UserEntity updateCredentials(Long loggedUserId, String email, String password) {
		
		HashMap<String, String> errors = new HashMap<String, String>();


		UserEntity savedLoggedUser = userRep.findById(loggedUserId)
				.orElseThrow(() -> new AppException("No se ha podido recuperar el usuario logeado", HttpStatus.NOT_FOUND));

		
		if (savedLoggedUser.getEmail().equals(email))
			errors.put("email", "No puede introducir el mismo correo");
		else if (!savedLoggedUser.getEmail().equals(email) && userRep.existsByEmail(email))
			errors.put("email", "El correo introducido se encuentra en uso");
	
		
		if (savedLoggedUser.getPassword().equals(password))
			errors.put("password", "No puedes introducir la misma contraseña");
		
		if (!errors.isEmpty()) 
			throw new AppMultipleErrosException(errors, HttpStatus.BAD_REQUEST);
			

		savedLoggedUser.setEmail(email);
		savedLoggedUser.setPassword(password);
		UserEntity updatedLoggedUser = userRep.save(savedLoggedUser);
		return updatedLoggedUser;
	}

}