package com.dev.sirasa.data.remote.response.booking

import com.google.gson.annotations.SerializedName

data class GetBookingsPaginateResponse(

	@field:SerializedName("data")
	val data: List<DataBookingsPaginate?>? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataBookingsPaginate(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("slots")
	val slots: List<SlotsItemPaginate?>? = null,

	@field:SerializedName("roomCapacity")
	val roomCapacity: Int? = null,

	@field:SerializedName("roomFloor")
	val roomFloor: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("roomId")
	val roomId: String? = null,

	@field:SerializedName("userName")
	val userName: String? = null,

	@field:SerializedName("userPhone")
	val userPhone: String? = null,

	@field:SerializedName("roomName")
	val roomName: String? = null,

	@field:SerializedName("participant")
	val participant: Int? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null
)

data class Meta(

	@field:SerializedName("next")
	val next: Int? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("perPage")
	val perPage: Int? = null,

	@field:SerializedName("lastPage")
	val lastPage: Int? = null,

	@field:SerializedName("prev")
	val prev: Any? = null,

	@field:SerializedName("currentPage")
	val currentPage: Int? = null
)

data class SlotsItemPaginate(

	@field:SerializedName("slotId")
	val slotId: String? = null,

	@field:SerializedName("startTime")
	val startTime: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("endTime")
	val endTime: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("date")
	val date: String? = null,
)
