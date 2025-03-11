package com.dev.sirasa.data.remote.response.booking

import com.google.gson.annotations.SerializedName

data class BookingValidationResponse(
    @field:SerializedName("data")
    val data: DataBooking? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)