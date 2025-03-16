package com.dev.sirasa.data.remote.response.booking

import com.dev.sirasa.data.remote.response.user.Meta
import com.google.gson.annotations.SerializedName

data class GetBookingsPaginateResponse(

	@field:SerializedName("data")
	val data: List<DataBooking?>? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
