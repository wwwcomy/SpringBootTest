package com.iteye.wwwcomy.orderservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iteye.wwwcomy.orderservice.entity.Order;
import com.iteye.wwwcomy.orderservice.repository.OrderRepository;

@RequestMapping("/orders")
@RestController
public class OrderController {

	@Autowired
	private OrderRepository or;

	@RequestMapping(method = RequestMethod.GET, path = "")
	public List<Order> listOrder() {
		return or.findAll();
	}

	@RequestMapping(method = RequestMethod.GET, path = "/{orderName}")
	public Order getOrder(@PathVariable String orderName) {
		return or.findByName(orderName);
	}
}
