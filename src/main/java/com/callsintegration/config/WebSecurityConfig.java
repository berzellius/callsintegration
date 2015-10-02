package com.callsintegration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.BasePasswordEncoder;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by berz on 26.10.14.
 */
@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().requireCsrfProtectionMatcher(
                new RequestMatcher() {
                    @Override
                    public boolean matches(HttpServletRequest httpServletRequest) {

                        if (new RegexRequestMatcher("rest*", null).matches(httpServletRequest)) {
                            return true;
                        }

                        return false;
                    }
                }
        );

        http
                .authorizeRequests()
                .antMatchers("/", "/index", "/rest/**").permitAll()
                .anyRequest().authenticated()
        ;


        http
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();


    }

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers("/static/**");
    }

    @Configuration
    protected static class AuthenticationConfiguration extends
            GlobalAuthenticationConfigurerAdapter {

        @Autowired
        UserDetailsService userDetailsService;

        UserDetailsService getUserDetailsService() {
            return this.userDetailsService;
        }

        @Autowired
        BasePasswordEncoder passwordEncoder;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder)
            ;
        }

    }

    @Bean
    BasePasswordEncoder basePasswordEncoder() {
        //return new PlaintextPasswordEncoder();
        return new Md5PasswordEncoder();
    }


}
