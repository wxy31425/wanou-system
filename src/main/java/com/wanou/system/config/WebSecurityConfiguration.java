package com.wanou.system.config;


import com.wanou.system.service.administratorService;
import com.wanou.system.tools.Encrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.PrintWriter;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final static Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfiguration.class);
    @Autowired
    administratorService administratorService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().and()
                .sessionManagement().sessionFixation().none().and()
                .authorizeRequests()

                .antMatchers("/login").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/services/**").permitAll()
                .antMatchers("/css/**", "/layui/**", "/images/**", "/js/**").permitAll()
                .antMatchers("/swagger-ui.html").authenticated()
                .antMatchers("/api/**").permitAll()

                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login")
                .successHandler((request,reponse, authentication) -> {
                 PrintWriter writer = reponse.getWriter();
                    writer.print(true);
                })
                .failureHandler(((request, response, exception) -> {

                    exception.printStackTrace();
                })).permitAll()
                .and().logout().logoutUrl("/loginOut")
                .permitAll();
        http.headers().frameOptions().sameOrigin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(administratorService)
                .passwordEncoder(passwordEncoder());
    }


    private PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                try {
                    return Encrypt.encryptRMS(charSequence.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                String mm = null;
                try {
                    mm = Encrypt.encryptRMS(charSequence.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                if (mm == null) {
                    return false;
                }
                return mm.equals(s);
            }
        };
    }
}
