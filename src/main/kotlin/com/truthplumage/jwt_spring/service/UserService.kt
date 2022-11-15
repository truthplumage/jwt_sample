package com.truthplumage.jwt_spring.service

import com.truthplumage.jwt_spring.util.JwtSecret
import com.truthplumage.jwt_spring.vo.JwtObject
import com.truthplumage.jwt_spring.vo.ResponseObject
import com.truthplumage.jwt_spring.vo.UserVo
import lombok.AllArgsConstructor
import org.springframework.stereotype.Service

@Service
@AllArgsConstructor
class UserService{
    lateinit var jwtSecret: JwtSecret
    fun makeJwtToken(userVo: UserVo):ResponseObject<JwtObject>{
        var res = ResponseObject<JwtObject>()
        var jwtObject = JwtObject()
        jwtObject.jwt = jwtSecret.makeJwtToken(userVo, JwtSecret.minutesDay)!!
        jwtObject.jwtExpireToken = jwtSecret.makeJwtToken(userVo, JwtSecret.minutesWeek)!!
        return res
    }
}