package alex.zhurkov.gifloader.data.remote

import alex.zhurkov.gifloader.data.remote.model.GifResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyRemoteSource {
    @GET("gifs/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): GifResponse
}
