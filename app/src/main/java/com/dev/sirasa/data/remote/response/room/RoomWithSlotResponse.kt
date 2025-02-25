package com.dev.sirasa.data.remote.response.room

import com.google.gson.annotations.SerializedName

data class RoomWithSlotResponse(

	@field:SerializedName("data")
	val data: List<DataRoomDetail?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)


