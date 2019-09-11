package com.test.service.impl;

import java.util.Collection;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.test.entity.User;
import com.test.service.UserService;

@Service
//@Component
public class UserServiceImpl implements UserService {

	@Override
	public User createUser(User user) {
		user.setId(new Random().nextLong());
		return user;
	}

	@Override
	public User getUser(long id) {
		return new User(id, "A new User created by UserServiceImpl");
	}

	@Override
	public Collection<User> listUsers() {
		return null;
	}

}
