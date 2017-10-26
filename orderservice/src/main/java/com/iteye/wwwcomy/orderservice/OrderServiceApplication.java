package com.iteye.wwwcomy.orderservice;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

import com.iteye.wwwcomy.orderservice.entity.Order;
import com.iteye.wwwcomy.orderservice.repository.OrderRepository;

@SpringBootApplication
@EnableOAuth2Sso
public class OrderServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	public static class SecurityConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/**").authorizeRequests().antMatchers("/", "/js/**", "/login").permitAll().anyRequest()
					.authenticated().and().logout().logoutSuccessUrl("/").permitAll().and().csrf().disable();
		}
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