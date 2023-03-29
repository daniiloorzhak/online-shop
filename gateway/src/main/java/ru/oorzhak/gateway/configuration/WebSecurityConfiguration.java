package ru.oorzhak.gateway.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.oorzhak.gateway.security.JwtVerifierFilter;


@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfiguration {

    private final JwtVerifierFilter jwtVerifierFilter;

    @Autowired
    public WebSecurityConfiguration(JwtVerifierFilter jwtTokenFilter) {
        this.jwtVerifierFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/auth/getAccessToken").authenticated();
                    auth.requestMatchers("/auth/**").permitAll();
                })
        ;

        http.addFilterBefore(jwtVerifierFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
