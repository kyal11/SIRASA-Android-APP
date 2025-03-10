package com.dev.sirasa.data.remote.response.booking

import com.google.gson.annotations.SerializedName

data class BookingSummaryResponse(

	@field:SerializedName("data")
	val data: DataSummary? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataSummary(

	@field:SerializedName("doneBookings")
	val doneBookings: Int? = null,

	@field:SerializedName("totalBookings")
	val totalBookings: Int? = null,

	@field:SerializedName("canceledBookings")
	val canceledBookings: Int? = null,

	@field:SerializedName("bookedBookings")
	val bookedBookings: Int? = null,

	@field:SerializedName("totalRooms")
	val totalRooms: Int? = null
)
