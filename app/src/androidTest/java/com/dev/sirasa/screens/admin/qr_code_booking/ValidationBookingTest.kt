package com.dev.sirasa.screens.admin.qr_code_booking

import com.dev.sirasa.screens.user.home.BookingResult
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dev.sirasa.data.local.UserModel
import com.dev.sirasa.data.local.UserPreference
import com.dev.sirasa.data.local.dataStore
import com.dev.sirasa.data.remote.response.booking.CreateBookingRequest
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
class ValidationBookingTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var context: Context

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
            email = "admin@example.com",
            name = "Admin User",
            nim = "001",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIwMzZjNjcwMC1kOWI2LTRmYTItOTA4YS00YjBjZTczNDI5NDkiLCJuYW1lIjoiQWRtaW4gVXNlciIsImVtYWlsIjoiYWRtaW5AZXhhbXBsZS5jb20iLCJyb2xlIjoiYWRtaW4iLCJpc1ZlcmlmaWVkIjp0cnVlLCJpYXQiOjE3NDg2MjE0MDcsImV4cCI6MTc0ODc5NDIwN30.EnjjMAodLneRCf-HSn4_hlv0o96lr_iM1mV4hxe31d0"
        )
        userPreference.saveSession(dummyUser)
    }

    @Test
    fun validasiPeminjaman_sukses() = runTest {
        Log.d("TestValidasi", "Mulai test validasiPeminjaman_sukses")
        bookingRepository.getActiveBooking().collect { response ->
            Log.d("TestValidasi", "Response getActiveBooking: status=${response.status}, data=${response.data}")
            assertEquals("success", response.status)
            assertNotNull(response.data)
            assertTrue(response.data!!.isNotEmpty())

            val decodeQrBooking = response.data!![0].id
            Log.d("TestValidasi", "Booking ID untuk validasi: $decodeQrBooking")

            bookingRepository.validationBooking(decodeQrBooking).collect { response ->
                Log.d("TestValidasi", "Response validationBooking: status=${response.status}, data=${response.data}")
                assertEquals("success", response.status)
                assertNotNull(response.data)
            }
        }
    }

    @Test
    fun validasiPeminjaman_gagal() = runTest {
        Log.d("TestValidasi", "Mulai test validasiPeminjaman_gagal")
        bookingRepository.getHistoryBookingUser().collect { response ->
            Log.d("TestValidasi", "Response getHistoryBookingUser: status=${response.status}, data=${response.data?.size}")
            assertEquals("success", response.status)
            assertNotNull(response.data)
            assertTrue(response.data!!.isNotEmpty())

            val decodeQrBookingCancel = response.data.filter { it.status == "cancel" }
            Log.d("TestValidasi", "Jumlah booking dengan status 'cancel': ${decodeQrBookingCancel.size}")

            if (decodeQrBookingCancel.isNotEmpty()) {
                try {
                    val bookingId = decodeQrBookingCancel[0].id
                    Log.d("TestValidasi", "Mencoba validasi booking yang sudah 'cancel': ID=$bookingId")
                    bookingRepository.validationBooking(bookingId).collect { response ->
                        Log.d("TestValidasi", "Response validationBooking (unexpected): ${response.status}")
                        fail("Expected HTTP 404 error, but got success response")
                    }
                } catch (e: retrofit2.HttpException) {
                    Log.e("TestValidasi", "Exception HTTP error code: ${e.code()}, message: ${e.message()}")
                    assertEquals(404, e.code())
                }
            } else {
                Log.w("TestValidasi", "Tidak ada booking dengan status 'done' untuk diuji")
                fail("Tidak ada booking dengan status 'done' untuk diuji validasi gagal")
            }
        }
    }



}
