package com.dev.sirasa.data.remote.response.booking

import com.dev.sirasa.data.remote.response.room.DataRoom
import com.dev.sirasa.data.remote.response.user.DataUser
import com.google.gson.annotations.SerializedName

data class CreateBookingResponse(

	@field:SerializedName("data")
	val data: DataBooking? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class SlotBooking(

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

data class BookingSlotItem(

	@field:SerializedName("slotId")
	val slotId: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("slot")
	val slot: SlotBooking? = null,

	@field:SerializedName("bookingId")
	val bookingId: String? = null
)

data class DataBooking(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("bookingSlot")
	val bookingSlot: List<BookingSlotItem?>? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("user")
	val user: DataUser? = null,

	@field:SerializedName("room")
	val room: DataRoom? = null,

	@field:SerializedName("roomId")
	val roomId: String? = null,

	@field:SerializedName("participant")
	val participant: Int? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class CreateBookingRequest(
	val roomId: String,
	val bookingSlotId: List<String>,
	val participant: Int,
	val description: String?
)

open class BaseBookingResponse(
	open val status: String?,
	open val message: String?
)
