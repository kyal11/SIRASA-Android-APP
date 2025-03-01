package com.dev.sirasa.data.remote.response.room

import com.google.gson.annotations.SerializedName

data class RoomResponse(

	@field:SerializedName("data")
	val data: List<DataRoom?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataRoom(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("startTime")
	val startTime: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("endTime")
	val endTime: String? = null,

	@field:SerializedName("floor")
	val floor: Int? = null,

	@field:SerializedName("capacity")
	val capacity: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
