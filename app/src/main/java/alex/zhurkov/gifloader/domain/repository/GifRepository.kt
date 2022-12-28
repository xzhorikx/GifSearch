package alex.zhurkov.gifloader.domain.repository

import alex.zhurkov.gifloader.domain.model.GifPage

interface GifRepository {
    suspend fun searchGifs(query: String, offset: Int, limit: Int): GifPage
}
