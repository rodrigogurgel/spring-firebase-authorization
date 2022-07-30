# Spring Firebase Authorization

Resource server using firebase authentication

## Firebase Setup

First step, needs to create a new firebase project in [firebase console](https://console.firebase.google.com/u/0/?hl=pt)
and enable authentication (for this project, I will enable email and password)

### Creating user (email and password) to test

Take api key in firebase project settings and send request to this curl (for more details take a look on
this [firebase documentation](https://firebase.google.com/docs/reference/rest/auth#section-create-email-password)):

```bash
curl 'https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=[API_KEY]' \
-H 'Content-Type: application/json' \
--data-binary '{"email":"[user@example.com]","password":"[PASSWORD]","returnSecureToken":true}'
```

To authenticate after create user, use this curl (for more details take a look on
this [firebase documentation](https://firebase.google.com/docs/reference/rest/auth#section-sign-in-email-password)):

```
curl 'https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=[API_KEY]' \
-H 'Content-Type: application/json' \
--data-binary '{"email":"[user@example.com]","password":"[PASSWORD]","returnSecureToken":true}'
```

## Spring Resource Server Setup

A resource server is a server for access-protected resources. It handles authenticated requests from an app that has an
access token.

## Creating Resource Server Configuration

First create a new configuration class to configure authenticated and unauthenticated routes and annotate the class with
`@EnableWebSecurity`, example:

```kotlin
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableWebSecurity
class ResourceServerConfiguration {}
```

Create a bean to `SecurityFilterChain`:

```kotlin
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
class ResourceServerConfiguration {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.build()
    }
}
```

and now configure your private and public routes and set `oauth2ResourceServer`:

```kotlin
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
```

### Configure application.yaml to use google jwk

To use authorize firebase token, paste follow configuration inside application.yaml file

Note: take project code in firebase project settings and create a new environment variable with name `PROJECT_CODE` this
code inside application.yaml

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://securetoken.google.com/${PROJECT_CODE}
          jwk-set-uri: https://www.googleapis.com/robot/v1/metadata/jwk/securetoken@system.gserviceaccount.com
          audiences: ${PROJECT_CODE}
```