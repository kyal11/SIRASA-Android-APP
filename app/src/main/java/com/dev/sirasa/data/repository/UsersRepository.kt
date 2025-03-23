package com.dev.sirasa.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dev.sirasa.data.remote.paging.UsersPagingSource
import com.dev.sirasa.data.remote.response.crm.SummaryDataResponse
import com.dev.sirasa.data.remote.response.user.CreateUser
import com.dev.sirasa.data.remote.response.user.DataUser
import com.dev.sirasa.data.remote.response.user.DeleteUserResponse
import com.dev.sirasa.data.remote.response.user.GetDetailUserResponse
import com.dev.sirasa.data.remote.response.user.UpdateAccount
import com.dev.sirasa.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val apiService: ApiService
){
    suspend fun getDetailAccount() : Flow<GetDetailUserResponse> = flow  {
        emit(apiService.getDetailUser())
    }
    suspend fun createUser(createUser: CreateUser): Flow<GetDetailUserResponse> = flow {
        emit(apiService.createUsers(createUser))
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
        updateData.verified?.let {
            val jsonVerified = "{\"verified\":$it}".toRequestBody("application/json".toMediaTypeOrNull())
            val verifiedPart = MultipartBody.Part.createFormData("verified", null, jsonVerified)
            parts.add(verifiedPart)
        }
        updateData.role?.let {
            parts.add(MultipartBody.Part.createFormData("role", it))
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

    suspend fun updateUserById(
        userId: String,
        updateData: UpdateAccount,
        imageFile: File? = null
    ): Flow<GetDetailUserResponse> = flow {
        if (updateData.verified != null) {
            emit(apiService.updateUsersVerified(userId, updateData))
        } else {
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
            updateData.role?.let {
                parts.add(MultipartBody.Part.createFormData("role", it))
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

            emit(apiService.updateUsers(userId, parts))
        }
    }

    suspend fun getSummary(): Flow<SummaryDataResponse> = flow {
        emit(apiService.summaryData())
    }
    suspend fun getUserById(userId: String): Flow<GetDetailUserResponse> = flow {
        emit(apiService.getUserById(userId))
    }
    suspend fun deleteUserById(userId: String): Flow<DeleteUserResponse> = flow {
        emit(apiService.deleteUsers(userId))
    }
    fun getPaginatedUsers(
        searchQuery: String? = null,
        role: String? = null
    ): Flow<PagingData<DataUser>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UsersPagingSource(apiService, searchQuery, role) }
        ).flow
    }

    fun downloadUsersExcelFlow(startDate: String?, endDate: String?): Flow<Response<ResponseBody>> = flow {
        emit(apiService.downloadUsersExcel(startDate, endDate))
    }
}
