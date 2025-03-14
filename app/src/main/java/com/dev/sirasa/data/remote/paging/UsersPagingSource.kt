package com.dev.sirasa.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dev.sirasa.data.remote.response.user.DataUser
import com.dev.sirasa.data.remote.retrofit.ApiService
import retrofit2.HttpException
import java.io.IOException

class UsersPagingSource(
    private val apiService: ApiService,
    private val search: String? = null,
    private val role: String? = null
) : PagingSource<Int, DataUser>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataUser> {
        return try {
            val page = params.key ?: 1
            val perPage = params.loadSize.coerceAtMost(PAGE_SIZE)

            val response = apiService.getAllUsersPaginate(
                page = page,
                perPage = perPage,
                search = search,
                role = role
            )

            val userList = response.data
            val nextPage = response.meta?.next

            LoadResult.Page(
                data = userList,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextPage
            )
        } catch (e: IOException) {
            LoadResult.Error(e) // Masalah jaringan
        } catch (e: HttpException) {
            LoadResult.Error(e) // Error dari server
        } catch (e: Exception) {
            LoadResult.Error(e) // Error lainnya
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DataUser>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.nextKey?.minus(1) ?: anchorPage?.prevKey?.plus(1)
        }
    }

    companion object {
        private const val PAGE_SIZE = 3
    }
}