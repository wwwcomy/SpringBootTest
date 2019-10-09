package com.iteye.wwwcomy.authservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * This is the authorization and resource server, as Spring boot 1.5.* + Spring
 * security oauth2 is no longer the trend, instead, Spring Security 5 will be as
 * the replacement.
 * 
 * @see https://spring.io/blog/2018/01/30/next-generation-oauth-2-0-support-with-spring-security
 *      I'll look into Spring Boot 2.X + Spring Security 5. This project will be
 *      deprecated.
 * @author xingnliu
 */
@SpringBootApplication
@EnableResourceServer
@EnableAuthorizationServer
@EnableWebSecurity
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

/**
 *
 * Really don't understand why I should use ACCESS_OVERRIDE_ORDER to make it
 * work
 * 
 * @see https://stackoverflow.com/questions/49970346/correctly-configure-spring-security-oauth2
 * @see https://github.com/spring-projects/spring-security-oauth/issues/980
 * 
 *      From what I understand, using spring security OAuth2, multiple
 *      configurations are done to protect different endpoints, like authorize
 *      endpoint, user endpoint, token endpoint, login endpoint.
 * 
 *      Such kind of endpoints are actually defined in different configurations
 *      with different filters enabled, which, might have conflicts.
 * 
 *      That's why it is so hard to make it work...
 * @author xingnliu
 */
@Configuration
//@Order(-20)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin().loginPage("/login").permitAll().and().authorizeRequests().antMatchers("/login*").permitAll()
				.anyRequest().authenticated().and().csrf().disable();
	}
	// .antMatchers("/oauth/token").permitAll()
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests().antMatchers("/**").authenticated().and().formLogin().loginPage("/login").permitAll()
//				.and().logout().permitAll();
//	}

}

@Configuration
class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId("USERS");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
		// Since we want the protected resources to be accessible in the UI as well we
		// need
//				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
				.requestMatchers().antMatchers("/api/**").and().authorizeRequests()
//                .antMatchers("/product/**").access("#oauth2.hasScope('select') and hasRole('ROLE_USER')")
				.antMatchers("/user").authenticated();
	}
}

@Configuration
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
				.authorizedGrantTypes("authorization_code", "refresh_token", "password", "client_credentials")
				.scopes("openid").redirectUris("http://wwwcomy.com:8080/login").autoApprove(true);
	}

//	@Override
//	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//		endpoints.authenticationManager(authenticationManager).accessTokenConverter(jwtAccessTokenConverter());
//	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.allowFormAuthenticationForClients();
//		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

}
