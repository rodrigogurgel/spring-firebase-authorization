package br.com.rodrigogurgel.springfirebaseauthorization.configurations

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
class ResourceServerConfiguration {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .authorizeHttpRequests { authorize ->
                authorize
                    .antMatchers(
                        HttpMethod.GET,
                        "/anonymous"
                    ).permitAll()
                    .anyRequest()
                    .authenticated()
            }
            .oauth2ResourceServer { oauth -> oauth.jwt() }
        return http.build()
    }
}