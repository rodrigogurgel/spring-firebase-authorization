package br.com.rodrigogurgel.springfirebaseauthorization.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class ResourceServerConfiguration {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers(
                        HttpMethod.GET,
                        "/anonymous"
                    ).permitAll()
                    .anyRequest()
                    .authenticated()
            }
            .oauth2ResourceServer { oauth -> oauth.jwt(Customizer.withDefaults()) }
        return http.build()
    }
}