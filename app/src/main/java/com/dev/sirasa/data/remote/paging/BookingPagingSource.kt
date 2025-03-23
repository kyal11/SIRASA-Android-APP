package com.dev.sirasa.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dev.sirasa.data.remote.response.booking.DataBooking
import com.dev.sirasa.data.remote.response.booking.DataBookingsPaginate
import com.dev.sirasa.data.remote.retrofit.ApiService
import retrofit2.HttpException
import java.io.IOException

class BookingPagingSource(
    private val apiService: ApiService,
    private val startDate: String? = null,
    private val endDate: String? = null,
    private val status: String? = null,
    private val search: String? = null
) : PagingSource<Int, DataBookingsPaginate>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataBookingsPaginate> {
        return try {
            val page = params.key ?: 1
            val perPage = params.loadSize.coerceAtMost(PAGE_SIZE)

            val response = apiService.getBookingsPaginate(
                page = page,
                perPage = perPage,
                search = search,
                startDate = startDate,
                endDate = endDate,
                status = status
            )

            val bookingList = response.data?.filterNotNull() ?: emptyList()
            val nextPage = if (bookingList?.isEmpty() == true) null else page + 1

            LoadResult.Page(
                data = bookingList,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextPage
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DataBookingsPaginate>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.nextKey?.minus(1) ?: anchorPage?.prevKey?.plus(1)
        }
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}
