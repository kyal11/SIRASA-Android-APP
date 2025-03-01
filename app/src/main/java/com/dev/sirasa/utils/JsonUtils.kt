package com.dev.sirasa.utils

import org.json.JSONException
import org.json.JSONObject

object JsonUtils {
    fun extractMessageFromJson(jsonString: String?): String? {
        return try {
            val jsonObject = JSONObject(jsonString)
            jsonObject.getString("message")
        } catch (e: JSONException) {
            null
        }
    }
}