package com.gnk2so.auth.user.service;

import com.gnk2so.auth.user.model.User;

public interface UserService {

	User save(User user);
	User findByEmail(String email);
	User findById(Long id);

}
