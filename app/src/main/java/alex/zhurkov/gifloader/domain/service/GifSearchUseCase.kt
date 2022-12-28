package alex.zhurkov.gifloader.domain.service

import alex.zhurkov.gifloader.domain.model.GifPage

interface GifSearchUseCase {
    suspend fun searchGifs(query: String, offset: Int): GifPage
}
