package com.dev.sirasa.data.remote.response.crm

import com.google.gson.annotations.SerializedName

data class SummaryDataResponse(

	@field:SerializedName("data")
	val data: DataCrm? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class User(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("totalRoleUser")
	val totalRoleUser: Int? = null,

	@field:SerializedName("totalRoleSuperadmin")
	val totalRoleSuperadmin: Int? = null,

	@field:SerializedName("totalRoleAdmin")
	val totalRoleAdmin: Int? = null
)

data class RoomsCrm(

	@field:SerializedName("dataRooms")
	val dataRooms: List<DataRoomsItemCrm?>? = null,

	@field:SerializedName("totalRooms")
	val totalRooms: Int? = null
)

data class DataRoomsItemCrm(

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

data class DataCrm(

	@field:SerializedName("rooms")
	val rooms: RoomsCrm? = null,

	@field:SerializedName("booking")
	val booking: BookingCrm? = null,

	@field:SerializedName("user")
	val user: User? = null
)

data class BookingCrm(

	@field:SerializedName("totalBooked")
	val totalBooked: Int? = null,

	@field:SerializedName("totalCancel")
	val totalCancel: Int? = null,

	@field:SerializedName("totalBooking")
	val totalBooking: Int? = null,

	@field:SerializedName("totalDone")
	val totalDone: Int? = null
)
