package com.dev.sirasa.data.remote.response.room

import com.google.gson.annotations.SerializedName

data class RoomDetailResponse(

	@field:SerializedName("data")
	val data: DataRoom? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataRoomDetail(

	@field:SerializedName("slots")
	val slots: List<SlotsDetailItem?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("startTime")
	val startTime: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("endTime")
	val endTime: String? = null,

	@field:SerializedName("floor")
	val floor: Int? = null,

	@field:SerializedName("capacity")
	val capacity: Int? = null
)

data class SlotsDetailItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("startTime")
	val startTime: String? = null,

	@field:SerializedName("isBooked")
	val isBooked: Boolean? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("endTime")
	val endTime: String? = null,

	@field:SerializedName("isExpired")
	val isExpired: Boolean? = null,

	@field:SerializedName("roomId")
	val roomId: String? = null
)
