package com.iteye.wwwcomy.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iteye.wwwcomy.orderservice.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	public Order findByName(String name);

}
