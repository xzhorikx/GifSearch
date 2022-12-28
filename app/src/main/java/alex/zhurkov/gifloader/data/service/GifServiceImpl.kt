package alex.zhurkov.gifloader.data.service

import alex.zhurkov.gifloader.domain.model.GifPage
import alex.zhurkov.gifloader.domain.repository.GifRepository
import alex.zhurkov.gifloader.domain.service.GifSearchUseCase

private const val PAGE_SIZE: Int = 25

class GifServiceImpl(
    private val gifRepository: GifRepository
) : GifSearchUseCase {
    override suspend fun searchGifs(query: String, offset: Int): GifPage =
        gifRepository.searchGifs(
            query = query,
            offset = offset,
            limit = PAGE_SIZE
        )
}
