package com.test.service.impl;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import com.test.entity.User;
import com.test.service.UserService;

@Component
public class UserServiceImpl2 implements UserService {

	private static Map<Long, User> registry = new ConcurrentHashMap<Long, User>();
	private static AtomicLong al = new AtomicLong();

	@Override
	public User createUser(User user) {
		long id = al.incrementAndGet();
		user.setId(id);
		registry.put(id, user);
		return user;
	}

	@Override
	public User getUser(long id) {
		System.out.println("In method UserServiceImpl2.getUser");
		return registry.get(id);
	}

	@Override
	public Collection<User> listUsers() {
		return registry.values();
	}

}
