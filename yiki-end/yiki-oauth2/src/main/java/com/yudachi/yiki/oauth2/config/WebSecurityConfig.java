package com.yudachi.yiki.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * - @EnableGlobalMethodSecurity：该注解是用来开启权限注解
 * - prePostEnabled = true：开启Spring Security提供的四个权限注解，@PostAuthorize、@PostFilter、@PreAuthorize 以及@PreFilter
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService yikiUserDetailsService;

//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//        inMemoryUserDetailsManager.createUser(User.withUsername("root").password(passwordEncoder.encode("123")).roles("ADMIN").build());
//        return inMemoryUserDetailsManager;
//    }

    //去除security的ROLE_前缀限制
    @Bean
    public RoleVoter roleVoter(){
        RoleVoter roleVoter = new RoleVoter();
        roleVoter.setRolePrefix("");
        return roleVoter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
    }

    // 自定义AuthenticationManager:并没有在工厂中暴露出来
    // 使用AuthenticationManagerBuilder来自定义AuthenticationManager，覆盖默认的AuthenticationManager
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(yikiUserDetailsService);
    }

    // 开发者如需使用AuthenticationManager，
    // 则可以通过覆盖此方法，将configure(AuthenticationManagerBuilder)方法构造的AuthenticationManager暴露为Bean。
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
