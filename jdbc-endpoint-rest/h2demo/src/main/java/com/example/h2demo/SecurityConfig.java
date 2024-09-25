package com.example.h2demo;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    DataSource dataSource(){
        return new EmbeddedDatabaseBuilder()
        .setName("banco")
        .setType(EmbeddedDatabaseType.H2)
        .addScripts("/bd/simple-schema.sql", "/bd/useless-data.sql")
        .build();
    }

    @Bean
    //O tipo do retorno poderia ser UserDetailsManager também
    UserDetailsService users(){
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource());
        users.setUsersByUsernameQuery("select username,password,enabled from usuarios where username = ?");
        users.setAuthoritiesByUsernameQuery("select username,authority from autoridades where username = ?");
        return users;
    }

    @Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);

		return new ProviderManager(authenticationProvider);
	}

    @Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf((csrf) -> csrf.disable())
        .authorizeHttpRequests((authorize) -> authorize
        .requestMatchers("/h2-console/**","/login","/hello-world").permitAll()
        .requestMatchers("/hello-user").hasRole("USER")
        .requestMatchers("/hello-admin").hasRole("ADMIN"))
        .logout((logout) -> logout
        .logoutUrl("/logout-url")
        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.ACCEPTED)))
        .headers((headers) -> headers.frameOptions().sameOrigin());
        return http.build();
    }
}
