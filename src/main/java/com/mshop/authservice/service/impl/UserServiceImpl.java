package com.mshop.authservice.service.impl;

import com.mshop.authservice.entity.User;
import com.mshop.authservice.repository.UserRepository;
import com.mshop.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	
	public List<User> findByStatusTrueAndRoleFalse() {
	    return userRepository.findByStatusTrueAndRoleFalse();
	}
	
	@Transactional
	public User save(User user) {
	    return userRepository.save(user);
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public Boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	
	public List<User> findAllAdmin() {
		return userRepository.findAllAdmin();
	}
	
	public List<User> findAllUser() {
		return userRepository.findAllUser();
	}
	
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	public List<User> findAllByIds(List<Long> ids) {
		return userRepository.findAllById(ids);
	}
	
	public Boolean existsById(Long id) {
		return userRepository.existsById(id);
	}
}
