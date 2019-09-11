package com.test.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.test.entity.User;
import com.test.service.UserService;

@RestController
public class UserController {

	UserService userService;

	@RequestMapping(method = RequestMethod.POST, path = "/users")
	public User createUser(@RequestBody User user) {
		return userService.createUser(user);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/users")
	public Collection<User> listUsers() {
		return userService.listUsers();
	}

	@RequestMapping(method = RequestMethod.GET, path = "/users/{id}")
	public User getUser(@PathVariable long id, HttpServletResponse response) {
		User user = userService.getUser(id);
		if (user == null) {
			// Best practice is to use the @ExceptionHandler and @ResponseStatus
			// for global exception handling, use @ControllerAdvice
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}
		return userService.getUser(id);
	}

	public UserService getUserService() {
		return userService;
	}

	// @Resource
	@Autowired
	@Qualifier("userServiceImpl2")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
