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

@Configuration
//@Order(-20)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER) 
class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.formLogin().loginPage("/login").permitAll().and().authorizeRequests().antMatchers("/login*").permitAll()
//				.anyRequest().authenticated().and().csrf().disable();
//	}
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests().antMatchers("/**").authenticated()
                .and().formLogin().loginPage("/login").permitAll()
                .and().logout().permitAll();
    }

}

@Configuration
class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId("USERS");
		//.stateless(true);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				// Since we want the protected resources to be accessible in the UI as well we
				// need
				// session creation to be allowed (it's disabled by default in 2.0.6)
//				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
				.requestMatchers()
                .antMatchers("/**")
				.and().authorizeRequests()
//                .antMatchers("/product/**").access("#oauth2.hasScope('select') and hasRole('ROLE_USER')")
				.antMatchers("/user").authenticated();// 配置 order 访问控制，必须认证过后才可以访问
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
