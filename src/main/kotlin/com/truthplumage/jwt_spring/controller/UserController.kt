package com.truthplumage.jwt_spring.controller

import com.truthplumage.jwt_spring.service.UserService
import com.truthplumage.jwt_spring.vo.JwtObject
import com.truthplumage.jwt_spring.vo.ResponseObject
import com.truthplumage.jwt_spring.vo.UserVo
import lombok.AllArgsConstructor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@AllArgsConstructor
class UserController{
    lateinit var userService: UserService
    @PostMapping("/login")
    fun login(@RequestBody userVo: UserVo):ResponseObject<JwtObject>{
        return userService.makeJwtToken(userVo)
    }
}