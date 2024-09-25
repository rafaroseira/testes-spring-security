package com.example.h2demo;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf((csrf) -> csrf.disable())
        .authorizeHttpRequests((authorize) -> authorize.requestMatchers("/h2-console/**").permitAll()
        .anyRequest().authenticated())
        .headers((headers) -> headers.frameOptions().sameOrigin())
        .httpBasic();
        return http.build();
    }
}
