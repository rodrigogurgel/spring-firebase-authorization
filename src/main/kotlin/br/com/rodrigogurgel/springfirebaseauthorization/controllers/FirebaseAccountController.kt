package br.com.rodrigogurgel.springfirebaseauthorization.controllers

import java.security.Principal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class FirebaseAccountController {

    @GetMapping("/anonymous")
    fun anonymous(): String = "ok"

    @GetMapping("/authenticated")
    fun authenticated(principal: Principal): Principal = principal
}