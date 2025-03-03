package com.dev.sirasa.data.remote.response.booking

import com.google.gson.annotations.SerializedName

data class BookingUserResponse(

	@field:SerializedName("data")
	val data: List<DataHistory>,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataHistory(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("bookingSlot")
	val bookingSlot: List<BookingSlotItem?>? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("roomId")
	val roomId: String? = null,

	@field:SerializedName("participant")
	val participant: Int? = null,

	@field:SerializedName("room")
	val room: Room? = null,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class Room(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

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
	val capacity: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class Slot(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("startTime")
	val startTime: String? = null,

	@field:SerializedName("isBooked")
	val isBooked: Boolean? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("endTime")
	val endTime: String? = null,

	@field:SerializedName("bookingIdSlot")
	val bookingIdSlot: Any? = null,

	@field:SerializedName("isExpired")
	val isExpired: Boolean? = null,

	@field:SerializedName("roomId")
	val roomId: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
