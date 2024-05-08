package com.mshop.authservice.service;


import com.mshop.authservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
	
	public List<User> findByStatusTrueAndRoleFalse();
	
	public User findByEmail(String email);
	
	public Boolean existsByEmail(String email);	
	
	public List<User> findAllAdmin();
	
	public List<User> findAllUser();

	public List<User> findAllByIds(List<Long> ids);

	public User save(User user);
	
	public Boolean existsById(Long id);
	
	public Optional<User> findById(Long id);
}
