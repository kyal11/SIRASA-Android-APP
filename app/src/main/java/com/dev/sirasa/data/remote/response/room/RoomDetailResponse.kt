package com.dev.sirasa.data.remote.response.room

import com.google.gson.annotations.SerializedName

data class RoomDetailResponse(

	@field:SerializedName("data")
	val data: DataRoomDetail? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataRoomDetail(

	@field:SerializedName("slots")
	val slots: List<SlotsDetailItem> ,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("startTime")
	val startTime: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("endTime")
	val endTime: String,

	@field:SerializedName("floor")
	val floor: Int,

	@field:SerializedName("capacity")
	val capacity: Int
)

data class SlotsDetailItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("startTime")
	val startTime: String,

	@field:SerializedName("isBooked")
	val isBooked: Boolean,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("endTime")
	val endTime: String? = null,

	@field:SerializedName("isExpired")
	val isExpired: Boolean? = null,

	@field:SerializedName("roomId")
	val roomId: String? = null
)
