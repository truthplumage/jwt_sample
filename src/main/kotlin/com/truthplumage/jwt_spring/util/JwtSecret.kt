package com.truthplumage.jwt_spring.util

import com.google.gson.Gson
import com.sun.tools.sjavac.Log.info
import com.truthplumage.jwt_spring.vo.UserVo
import io.jsonwebtoken.Header
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.nio.charset.Charset
import java.security.Key
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.time.Duration
import java.util.*

class JwtSecret {
    var keyPair: KeyPair? = null
    var publicKey: Key? = null
    var privateKey: Key? = null
    fun JWTSecret() {
        try {
            keyPair = KeyPairGenerator.getInstance(SignatureAlgorithm.RS256.familyName).genKeyPair()
            publicKey = keyPair?.public
            privateKey = keyPair?.private
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }

    /**
     * 토큰을 생성하는 함수
     * @param userInfo
     * jwt 에 포함될 데이터 id 및 auth 코드 값을 입력 할 userInfo
     * @return
     * 생성된 토큰.
     */
    fun makeJwtToken(userInfo: UserVo, min:Int): String? {
        val now = Date()
        //헤더의 타입(type)을 지정할 수 있습니다. jwt를 사용하기 때문에 Header.JWT_TYPE로 사용해줍니다.
        //등록된 클레임 중, 토큰 발급자(iss)를 설정할 수 있습니다.
        //등록된 클레임 중, 발급 시간(iat)를 설정할 수 있습니다. Date 타입만 추가가 가능합니다.
        //등록된 클레임 중, 만료 시간(exp)을 설정할 수 있습니다. 마찬가지로 Date 타입만 추가가 가능합니다.
        //비공개 클레임을 설정할 수 있습니다. (key-value)
        //해싱 알고리즘과 시크릿 키를 설정할 수 있습니다.
        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // (1)
            .setHeaderParam(Header.COMPRESSION_ALGORITHM, SignatureAlgorithm.RS256.value) // (1)
            .setIssuer("") // (2)
            .setIssuedAt(now) // (3)
            .setExpiration(
                Date(
                    now.time + Duration.ofMinutes(min.toLong())
                        .toMillis()
                )
            ) // (4)
            .claim("user_id", userInfo.id) // (5)
            .claim("user_name", userInfo.userName)
            .signWith(privateKey, SignatureAlgorithm.RS256) // (6)
            .compact()
    }

    companion object {
        const val minutesDay = 24 * 60
        const val minutesWeek = 24 * 60 * 7
    }

    /**
     * 토큰을 서명값이 정상인지 확인해주는 함수.
     * @param authorizationHeader
     * authorization 받아서 그대로 전달된 값
     * @throws JwtException
     * 파싱 중 오류가 발생시 error handle 로 전달.
     */
    fun parseJwtToken(authorizationHeader: String) {
        validationAuthorizationHeader(authorizationHeader) // (1)
        val token = extractToken(authorizationHeader) // (2)
        val jwt = Jwts.parserBuilder()
            .setSigningKey(publicKey) // (3)
            .build()
            .parse(token)

        info("isSign : " + Gson().toJson(jwt.body))
    }


    private fun validationAuthorizationHeader(header: String?) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw JwtException("header is null")
        }
    }

    private fun extractToken(authorizationHeader: String): String {
        return authorizationHeader.substring("Bearer ".length)
    }

    /**
     * 토큰이 만료되기 전에 초기화 시켜주는 작업.
     * @param token
     * 기존 토큰
     * @return
     * 초기화되어 새로운 토큰으로 발급.
     */
    fun refreshToken(token: String,min: Int): String? {
        parseJwtToken(token)
        val tokens = token.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val userInfo: UserVo = Gson().fromJson(
            String(Base64.getUrlDecoder().decode(tokens[1]), Charset.defaultCharset()),
            UserVo::class.javaObjectType
        )
        return makeJwtToken(userInfo, min)
    }

    /**
     * 토큰에 해당하는 유저 정보 가지고 오는 함수
     * @param token 토큰 정보
     * @return
     * 유저의 auth_cd, user_id 를 반환.
     */
    fun getUserInfoFromToken(token: String): UserVo? {
        val tokens = token.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return if (tokens.size == 3) {
            Gson().fromJson(
                String(Base64.getUrlDecoder().decode(tokens[1]), Charset.defaultCharset()),
                UserVo::class.javaObjectType
            )
        } else {
            null
        }
    }
}