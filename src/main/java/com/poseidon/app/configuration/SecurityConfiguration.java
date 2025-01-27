package com.poseidon.app.configuration;

import com.poseidon.app.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http
                .csrf()
                .ignoringAntMatchers("/api/**")

                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/log*").permitAll()
                .antMatchers("/create-account").permitAll()
                .antMatchers("/user/admin/*").hasAuthority("ADMIN")
                .antMatchers("/api/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login-error-account")
                .defaultSuccessUrl("/user/home")

                .and()
                .oauth2Login()
                .loginPage("/login")
                .failureUrl("/login-error-oauth2")
                .defaultSuccessUrl("/user/home")

                .and()
                .logout()
                .logoutSuccessUrl("/logout");
    }
}
