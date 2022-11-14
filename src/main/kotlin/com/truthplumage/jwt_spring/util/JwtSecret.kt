package com.truthplumage.jwt_spring.util

import io.jsonwebtoken.SignatureAlgorithm
import java.security.Key
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException

class JwtSecret {
    private val MINUTES = 24 * 60
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

}