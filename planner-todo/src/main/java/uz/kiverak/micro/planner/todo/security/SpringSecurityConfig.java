package uz.kiverak.micro.planner.todo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import uz.kiverak.micro.planner.utils.converter.KCRoleConverter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KCRoleConverter());

        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/reg/*").anonymous()
                        .requestMatchers("/admin/*").hasRole("admin")
                        .requestMatchers("/user/*").hasRole("user")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(customizer -> customizer
                        .jwt(jwtCustomizer -> jwtCustomizer.jwtAuthenticationConverter(jwtAuthenticationConverter))
                );

        return http.build();
    }

}
