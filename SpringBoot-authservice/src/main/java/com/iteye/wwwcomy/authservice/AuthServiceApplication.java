package com.iteye.wwwcomy.authservice;

import java.security.KeyPair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableResourceServer
public class AuthServiceApplication extends WebMvcConfigurerAdapter {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login.html");
		registry.addViewController("/oauth/confirm_access").setViewName("authorize");
	}

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}

@Configuration
@Order(-20)
class LoginConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin().loginPage("/login").permitAll().and().authorizeRequests()
				.antMatchers("/login*", "/oauth/authorize", "/oauth/confirm_access").permitAll().anyRequest()
				.authenticated().and().csrf().disable();
	}

}

@Configuration
@EnableAuthorizationServer
class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;

//	@Bean
//	public JwtAccessTokenConverter jwtAccessTokenConverter() {
//		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//		KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "foobar".toCharArray())
//				.getKeyPair("test");
//		converter.setKeyPair(keyPair);
//		return converter;
//	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("m1").secret("s1")
				.authorizedGrantTypes("authorization_code", "refresh_token", "password").scopes("openid")
				.autoApprove(true);
	}

//	@Override
//	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//		endpoints.authenticationManager(authenticationManager).accessTokenConverter(jwtAccessTokenConverter());
//	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

}

@Configuration
class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("m1").secret("s1").scopes("openid")
				.authorizedGrantTypes("authorization_code,refresh_token,password")
				.redirectUris("http://localhost:8080/login");
	}
}