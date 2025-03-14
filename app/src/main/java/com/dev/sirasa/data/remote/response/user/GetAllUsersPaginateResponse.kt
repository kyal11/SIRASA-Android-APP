package com.dev.sirasa.data.remote.response.user

import com.google.gson.annotations.SerializedName

data class GetAllUsersPaginateResponse(

	@SerializedName("data")
	val data: List<DataUser> = emptyList(), // Pastikan bukan nullable

	@SerializedName("meta")
	val meta: Meta = Meta(), // Pastikan tidak null agar aman

	@SerializedName("message")
	val message: String? = null,

	@SerializedName("status")
	val status: String? = null
)

data class Meta(

	@SerializedName("next")
	val next: Int? = null, // HARUS Int? bukan Any?

	@SerializedName("total")
	val total: Int? = null,

	@SerializedName("perPage")
	val perPage: Int? = null,

	@SerializedName("lastPage")
	val lastPage: Int? = null,

	@SerializedName("prev")
	val prev: Int? = null, // HARUS Int? bukan Any?

	@SerializedName("currentPage")
	val currentPage: Int? = null
)
