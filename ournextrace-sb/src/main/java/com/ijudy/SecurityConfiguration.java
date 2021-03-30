package com.ijudy;

import com.ijudy.races.security.AuthenticationFailureHandler;
import com.ijudy.races.security.AuthenticationSuccessHandler;
import com.ijudy.races.enums.RoleNames;
import com.ijudy.races.service.security.CustomOAuth2UserService;
import com.ijudy.races.service.security.CustomUserDetailsService;
import com.ijudy.races.service.security.LogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;
 
@Configuration
@EnableGlobalAuthentication
@EnableOAuth2Client
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
@Profile("!test")
/**
 * @see https://spring.io/guides/tutorials/spring-boot-oauth2/ for Facebook authentication
 * @see http://www.littlebigextra.com/spring-boot-oauth2-tutorial-for-authorizing-through-facebook-google-linkedin-and-twitter/
 * 
 * @author hugo
 *
 */
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	

	@Value( "${login.postsignin.url}")
	private String socialPostSignInUrl;
	
    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;


    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/",
                        "/login/**",
                        "/index.html",
                        "/oauth2/callback/**",
                        "/oauth2/code/**",
                        "/oauth2/authorize/**").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/error/**").permitAll()

                // Version 2.0

                // Util
                .antMatchers(HttpMethod.GET,"/api/v2/env/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v2/version/**").permitAll()
                // Race
                .antMatchers(HttpMethod.GET,"/api/v2/race/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v2/race/**").hasAuthority(RoleNames.USER.toString())
                .antMatchers(HttpMethod.PUT,  "/api/v2/race/**").hasAuthority(RoleNames.USER.toString())
                .antMatchers(HttpMethod.PATCH,"/api/v2/race/**").hasAuthority(RoleNames.USER.toString())
                .antMatchers(HttpMethod.DELETE,"/api/v2/race/**").hasAuthority(RoleNames.USER.toString())

                // Address
                .antMatchers(HttpMethod.GET,"/api/v2/address/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v2/addresses/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v2/address/**").hasAuthority(RoleNames.USER.toString())
                .antMatchers(HttpMethod.PATCH, "/api/v2/race-location/**").hasAuthority(RoleNames.USER.toString())
                .antMatchers(HttpMethod.PUT,  "/api/v2/address/**").hasAuthority(RoleNames.ADMIN.toString())
                .antMatchers(HttpMethod.PATCH,"/api/v2/address/**").hasAuthority(RoleNames.ADMIN.toString())
                .antMatchers(HttpMethod.DELETE,"/api/v2/address/**").hasAuthority(RoleNames.ADMIN.toString())

                // My Races
                .antMatchers("/api/v2/race-types/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v2/races/**").hasAuthority(RoleNames.ADMIN.toString())
                .antMatchers("/api/v2/myRace/**").hasAuthority(RoleNames.USER.toString())
                .antMatchers(HttpMethod.PATCH, "/api/v2/race-status/**").hasAuthority(RoleNames.USER.toString())

                // User
                .antMatchers(HttpMethod.GET,"/api/v2/user/**").hasAuthority(RoleNames.ADMIN.toString())
                .antMatchers(HttpMethod.PATCH,"/api/v2/user/**").hasAuthority(RoleNames.USER.toString())
                .antMatchers(HttpMethod.GET,"/api/v2/users/**").hasAuthority(RoleNames.ADMIN.toString())
                .antMatchers(HttpMethod.POST,"/api/v2/clone/**").hasAuthority(RoleNames.ADMIN.toString())
                .antMatchers("/api/v2/myRaces/**").permitAll()
                .antMatchers("/api/v2/contact/**").permitAll()
                .antMatchers("/api/v2/changePassword/**").permitAll()
                .antMatchers("/api/v2/forgot/**").permitAll()
                .antMatchers("/api/v2/user-exists").permitAll()
                .antMatchers("/api/v2/login/**").permitAll()
                .antMatchers("/api/v2/logout/**").permitAll()
                .antMatchers("/api/v2/registration").permitAll()
                .antMatchers("/api/v2/principal/**").permitAll()
                .antMatchers("/reset/**").hasAuthority("CHANGE_PASSWORD_PRIVILEGE")

                .anyRequest().authenticated().and()
                .csrf().disable()
                .cors().and()
                .formLogin()
                .loginProcessingUrl("/api/v2/login")	// NON SSO
                .successHandler(authenticationSuccessHandler)
                //.defaultSuccessUrl("/loginSuccess")
                .failureHandler(authenticationFailureHandler)
                //.failureUrl("/loginFailure");
                .usernameParameter("email")
                .passwordParameter("password")
                .and()
                .logout()
                .deleteCookies("remove")
                .invalidateHttpSession(true)
                .logoutUrl("/api/v2/logout")
                .logoutSuccessHandler(getLogoutSuccessHandler())
//   				.logoutSuccessUrl("/api/logoutOK")
                .and()
                .exceptionHandling().accessDeniedPage("/#/404")
                .and()
                .userDetailsService(customUserDetailsService)
                .oauth2Login()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .loginPage("/#/")
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(authorizationRequestRepository())
                .and()
                //.redirectionEndpoint()
                //.baseUri("/#/event/")
                //.and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

                // Add our custom Token based authentication filter
                http.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setHideUserNotFoundExceptions(false);
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
	@Bean
	public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
	    return new HttpSessionOAuth2AuthorizationRequestRepository();
	}
		
    @Bean
    @ConfigurationProperties("google.client")
    public AuthorizationCodeResourceDetails google() {
      return new AuthorizationCodeResourceDetails();
    }
    

    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResource() {
      return new ResourceServerProperties();
    }

    
    private Filter ssoFilter() {
    	  CompositeFilter filter = new CompositeFilter();
    	  List<Filter> filters = new ArrayList<>();
    	  
    	  // Google
    	  OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/oauth2/authorize");
    	  OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oauth2ClientContext);
    	  googleFilter.setRestTemplate(googleTemplate);
    	  
    	  UserInfoTokenServices tokenServices = new UserInfoTokenServices(googleResource().getUserInfoUri(), google().getClientId());
    	  tokenServices.setRestTemplate(googleTemplate);
    	  googleFilter.setTokenServices(tokenServices);

    	  
    	  filters.add(googleFilter);
    	  
    	  filter.setFilters(filters);
    	  return filter;
    }


    @Bean
    public LogoutSuccessHandler getLogoutSuccessHandler() {
        LogoutSuccessHandler handler = new LogoutSuccessHandler();
    	return handler;
    }
       
    @Override
    public void configure(WebSecurity web) {
            // Ignore these files from Angular /static JAR
        	web.ignoring()
                .antMatchers(   "/assets/**",
                                            "/resources/**",
                                            "/static/**",
                                            "/css/**",
                                            "/js/**",
                                            "/images/**",
                                            "/img/**")
                .regexMatchers(
                                ".*\\.txt",
                                ".*\\.eot",   ".*\\.ttf",
                				".*\\.svg", ".*\\.js",
                                ".*\\.css", ".*\\.scss",
                                ".*\\.jpg", ".*\\.jpeg",
                				".*\\.ico", ".*\\.png",
                                ".*\\.woff", ".*\\.woff2");
       	
    }
}
