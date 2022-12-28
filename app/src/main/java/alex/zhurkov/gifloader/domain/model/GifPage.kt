package alex.zhurkov.gifloader.domain.model

data class GifPage(
    val id: String,
    val query: String,
    val gifs: List<Gif>,
    val offset: Int,
    val totalCount: Int,
    val returnedCount: Int
)
