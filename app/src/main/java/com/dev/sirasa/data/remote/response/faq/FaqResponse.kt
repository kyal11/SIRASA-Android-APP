package com.dev.sirasa.data.remote.response.faq

import com.google.gson.annotations.SerializedName

data class FaqResponse(

	@field:SerializedName("data")
	val data: List<DataFaq?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataFaq(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("question")
	val question: String? = null,

	@field:SerializedName("answer")
	val answer: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
