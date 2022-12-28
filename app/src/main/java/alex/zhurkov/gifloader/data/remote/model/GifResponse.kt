package alex.zhurkov.gifloader.data.remote.model

import com.google.gson.annotations.SerializedName

data class GifResponse(
    @SerializedName("data") val data: List<GifData>,
    @SerializedName("pagination") val pagination: GifPaginationData
)

data class GifData(
    @SerializedName("id") val id: String,
    @SerializedName("images") val images: Images,
    @SerializedName("title") val title: String
) {

    data class Images(
        @SerializedName("fixed_height_small") val preview: GifData?,
    )

    data class GifData(
        @SerializedName("url") val url: String?,
        @SerializedName("width") val width: String?,
        @SerializedName("height") val height: String?
    )
}

data class GifPaginationData(
    @SerializedName("offset") val offset: Int,
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("count") val returnedCount: Int
)