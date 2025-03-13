package com.dev.sirasa.data.repository

import com.dev.sirasa.data.remote.response.crm.SummaryDataResponse
import com.dev.sirasa.data.remote.response.user.GetDetailUserResponse
import com.dev.sirasa.data.remote.response.user.UpdateAccount
import com.dev.sirasa.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val apiService: ApiService
){
    suspend fun getDetailAccount() : Flow<GetDetailUserResponse> = flow  {
        emit(apiService.getDetailUser())
    }

    suspend fun updateAccount(
        updateData: UpdateAccount,
        imageFile: File? = null
    ): Flow<GetDetailUserResponse> = flow {
        val parts = mutableListOf<MultipartBody.Part>()

        updateData.name?.let {
            parts.add(MultipartBody.Part.createFormData("name", it))
        }
        updateData.nim?.let {
            parts.add(MultipartBody.Part.createFormData("nim", it))
        }
        updateData.phoneNumber?.let {
            parts.add(MultipartBody.Part.createFormData("phoneNumber", it))
        }
        updateData.password?.let {
            parts.add(MultipartBody.Part.createFormData("password", it))
        }

        imageFile?.let { file ->
            val requestFile = file.asRequestBody()
            val imagePart = MultipartBody.Part.createFormData(
                "image_url",
                file.name,
                requestFile
            )
            parts.add(imagePart)
        }

        emit(apiService.updateAccount(parts))
    }

    suspend fun getSummary(): Flow<SummaryDataResponse> = flow {
        emit(apiService.summaryData())
    }
}
