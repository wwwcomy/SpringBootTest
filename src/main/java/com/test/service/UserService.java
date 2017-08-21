package com.test.service;

import java.util.Collection;

import com.test.entity.User;

public interface UserService {

	User createUser(User user);

	User getUser(long id);

	Collection<User> listUsers();
}
