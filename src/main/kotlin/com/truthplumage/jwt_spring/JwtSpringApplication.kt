package com.truthplumage.jwt_spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JwtSpringApplication

fun main(args: Array<String>) {
	runApplication<JwtSpringApplication>(*args)
}
