package com.dev.sirasa.screens.user.home

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dev.sirasa.data.local.UserModel
import com.dev.sirasa.data.local.UserPreference
import com.dev.sirasa.data.local.dataStore
import com.dev.sirasa.data.remote.response.booking.CreateBookingRequest
import com.dev.sirasa.data.remote.response.room.DataRoom
import com.dev.sirasa.data.repository.BookingRepository
import com.dev.sirasa.data.repository.RoomRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import javax.inject.Inject
import android.util.Log
import org.junit.Assert.fail

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CreateBookingTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var context: Context

    @Inject
    lateinit var roomRepository: RoomRepository

    @Inject
    lateinit var bookingRepository: BookingRepository

    private lateinit var userPreference: UserPreference

    @Before
    fun setup() {
        hiltRule.inject()
        context = ApplicationProvider.getApplicationContext()
        userPreference = UserPreference.getInstance(context.dataStore)

        runBlocking {
            setupDummySession()
        }
    }

    private suspend fun setupDummySession() {
        val dummyUser = UserModel(
            email = "2110511069@mahasiswa.upnvj.ac.id",
            name = "Rizky Al Arief",
            nim = "2110511069",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI3MjllZjA1NC1jZGE2LTQ0YzktODM2NS1mMWI4MGIwM2RkZjAiLCJuYW1lIjoiUml6a3kgQWwgQXJpZWYiLCJlbWFpbCI6IjIxMTA1MTEwNjlAbWFoYXNpc3dhLnVwbnZqLmFjLmlkIiwicm9sZSI6InVzZXIiLCJpc1ZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDg3NzQ0NjMsImV4cCI6MTc0ODk0NzI2M30.V3Cw9YAXF7tHx3PaxvJPzdzFXuct1riq68eYDB0VxUY"
        )
        userPreference.saveSession(dummyUser)
    }

    @Test
    fun membuatPeminjaman_sukses() = runTest {
        roomRepository.getRoomWithSlots("2").collect { response ->
            Log.d("Test Booking Success", "Response getRoomWithSlots: status=${response.status}, data=${response.data}")

            assertEquals("success", response.status)
            assertNotNull(response.data)
            assertTrue(response.data!!.isNotEmpty())

            val room = response.data!![0]
            val roomId = room?.id
            val availableSlots = room?.slots?.filter { it.isBooked == false }

            Log.d("Test Booking Success", "Room ID: $roomId")
            Log.d("Test Booking Success", "Available Slots: ${availableSlots?.map { it.id to it.isBooked }}")

            if (availableSlots != null) {
                assertTrue(availableSlots.isNotEmpty())
            }

            val selectedSlots = availableSlots.orEmpty()
                .take(1)
                .mapNotNull { it.id }

            Log.d("Test Booking Success", "Selected Slot IDs: $selectedSlots")

            val dataBookingTest = CreateBookingRequest(
                roomId = roomId!!,
                bookingSlotId = selectedSlots,
                participant = response.data!![0]!!.capacity,
                description = "Testing peminjaman sukses"
            )

            bookingRepository.createBooking(dataBookingTest).collect { result ->
                Log.d("Test Booking Success", "CreateBooking result: $result")

                when (result) {
                    is BookingResult.Success -> {
                        Log.d("Test Booking Success", "Booking berhasil: ${result.response}")
                        assertEquals("success", result.response.status)
                        assertNotNull(result.response.data)
                    }
                    else -> {
                        Log.e("Test Booking Success", "Booking gagal: $result")
                        fail("Expected Success, but got: $result")
                    }
                }
            }
        }
    }

    @Test
    fun membuatPeminjaman_rekomendasi() = runTest {
        roomRepository.getRoomWithSlots("2").collect { response ->
            Log.d("Test Booking Rekomendasi", "Response getRoomWithSlots: status=${response.status}, data=${response.data}")

            assertEquals("success", response.status)
            assertNotNull(response.data)
            assertTrue(response.data!!.isNotEmpty())

            val room = response.data!![0]
            val roomId = room?.id
            val availableSlots = room?.slots?.filter { it.isBooked == true }

            Log.d("Test Booking Rekomendasi", "Room ID: $roomId")
            Log.d("Test Booking Rekomendasi", "Available Slots: ${availableSlots?.map { it.id to it.isBooked }}")

            if (availableSlots != null) {
                assertTrue(availableSlots.isNotEmpty())
            }

            val selectedSlots = availableSlots.orEmpty()
                .take(1)
                .mapNotNull { it.id }

            Log.d("Test Booking Rekomendasi", "Selected Slot IDs: $selectedSlots")

            val dataBookingTest = CreateBookingRequest(
                roomId = roomId!!,
                bookingSlotId = selectedSlots,
                participant = response.data!![0]!!.capacity,
                description = "Testing peminjaman Rekomendasi"
            )

            bookingRepository.createBooking(dataBookingTest).collect { result ->
                Log.d("Test Booking Rekomendasi", "CreateBooking result: $result")

                when (result) {
                    is BookingResult.Recommendation -> {
                        Log.d("Test Booking Rekomendasi", "Booking berhasil: ${result.response}")
                        assertEquals("recommendation", result.response.status)
                        assertNotNull(result.response.data)
                    }
                    else -> {
                        Log.e("Test Booking Rekomendasi", "Booking gagal: $result")
                        fail("Expected recommendation, but got: $result")
                    }
                }
            }
        }
    }
}
