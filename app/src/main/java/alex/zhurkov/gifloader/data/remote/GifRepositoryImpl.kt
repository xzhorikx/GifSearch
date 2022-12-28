package alex.zhurkov.gifloader.data.remote

import alex.zhurkov.gifloader.domain.model.Gif
import alex.zhurkov.gifloader.domain.model.GifPage
import alex.zhurkov.gifloader.domain.repository.GifRepository

class GifRepositoryImpl(
    private val remoteSource: GiphyRemoteSource
) : GifRepository {
    override suspend fun searchGifs(query: String, offset: Int, limit: Int): GifPage {
        val result = remoteSource.search(
            query = query,
            offset = offset,
            limit = limit
        )
        return GifPage(
            id = query + offset,
            query = query,
            gifs = result.data.map {
                Gif(
                    id = it.id,
                    url = it.images.preview?.url.orEmpty()
                )
            },
            offset = result.pagination.offset,
            totalCount = result.pagination.totalCount,
            returnedCount = result.pagination.returnedCount
        )
    }
}