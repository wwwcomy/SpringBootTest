package com.iteye.wwwcomy.orderservice;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.iteye.wwwcomy.orderservice.entity.Order;
import com.iteye.wwwcomy.orderservice.repository.OrderRepository;

@SpringBootApplication
public class OrderServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}
}

@Component
class DummyDataClr implements CommandLineRunner {

	@Autowired
	OrderRepository orderRepository;

	@Override
	public void run(String... arg0) throws Exception {
		Stream.of("veh_order", "mat_order", "task_order", "pc_order")
				.forEach(orderName -> this.orderRepository.save(new Order(orderName)));
	}

}