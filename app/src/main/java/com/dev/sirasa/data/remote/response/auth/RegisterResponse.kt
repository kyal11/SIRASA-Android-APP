package com.dev.sirasa.data.remote.response.auth

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
	@field:SerializedName("data")
	val data: DataRegister? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataRegister(
	@field:SerializedName("nim")
	val nim: String? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("verified")
	val verified: Boolean? = null,

	@field:SerializedName("email")
	val email: String? = null
)
