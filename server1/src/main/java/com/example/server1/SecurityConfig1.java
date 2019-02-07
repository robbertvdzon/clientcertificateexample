package com.example.server1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(jsr250Enabled=true, securedEnabled=true, prePostEnabled=true)
@ComponentScan("com.example")
public class SecurityConfig1 extends WebSecurityConfigurerAdapter {


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("basicauth_client").password(passwordEncoder().encode("passwd"))
                .authorities("ROLE_USER");
//                .withUser("basicauth_client").password("passwd")
//                .authorities("ROLE_BA_USER");
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http
                .authorizeRequests()
                .antMatchers("/insecure").permitAll()
//                .antMatchers("/insecureAuthenticated")
//                .hasRole("BA_USER")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                .and()

//                .authorizeRequests()
//                .antMatchers("/insecure*")
//                .permitAll();
//                .and()



//                .antMatcher("/secure*")
//                .x509()
//                .subjectPrincipalRegex("CN=(.*?)(?:,|$)")
//                .userDetailsService(userDetailsService());

//                .and()
//                .antMatcher("/secureBAAuthenticated")
//                .httpBasic();

//                http.authorizeRequests().anyRequest().authenticated()
//                .and()
//                .httpBasic();




    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                if (username.equals("client")) {
                    return new User(username, "",
                            AuthorityUtils
                                    .commaSeparatedStringToAuthorityList("ROLE_CERTIFICATE_USER"));
                }
                return new User(username, "", Collections.emptyList());
            }
        };
    }
}
