package com.dev.sirasa.data.remote.response.booking

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(

	@field:SerializedName("data")
	val data: List<DataRecommendation?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataRecommendation(

	@field:SerializedName("slots")
	val slots: List<SlotsRecommendation?>? = null,

	@field:SerializedName("roomDate")
	val roomDate: String? = null,

	@field:SerializedName("roomId")
	val roomId: String? = null,

	@field:SerializedName("roomName")
	val roomName: String? = null
)

data class SlotsRecommendation(

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
