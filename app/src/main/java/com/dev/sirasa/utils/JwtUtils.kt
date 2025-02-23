package com.dev.sirasa.utils

import android.util.Log
import com.auth0.android.jwt.JWT

object JwtUtils {

    fun isVerified(token: String?): Boolean? {
        return try {
            token?.let {
                val jwt = JWT(it)
                jwt.getClaim("isVerified").asBoolean()
            }
        } catch (e: Exception) {
            Log.e("JwtUtils", "Error decoding JWT: ${e.message}")
            null
        }
    }

    fun getEmail(token: String?): String? {
        return try {
            token?.let {
                val jwt = JWT(it)
                jwt.getClaim("email").asString()
            }
        } catch (e: Exception) {
            Log.e("JwtUtils", "Error decoding JWT: ${e.message}")
            null
        }
    }

    fun getRole(token: String?): String? {
        return try {
            token?.let {
                val jwt = JWT(it)
                jwt.getClaim("role").asString()
            }
        } catch (e: Exception) {
            Log.e("JwtUtils", "Error decoding JWT: ${e.message}")
            null
        }
    }

    fun isTokenExpired(token: String?): Boolean {
        return try {
            token?.let {
                val jwt = JWT(it)
                jwt.isExpired(0)
            } ?: true
        } catch (e: Exception) {
            Log.e("JwtUtils", "Error decoding JWT: ${e.message}")
            true
        }
    }
}