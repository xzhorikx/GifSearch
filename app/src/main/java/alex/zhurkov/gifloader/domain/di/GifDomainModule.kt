package alex.zhurkov.gifloader.domain.di

import alex.zhurkov.gifloader.data.service.GifServiceImpl
import alex.zhurkov.gifloader.domain.repository.GifRepository
import alex.zhurkov.gifloader.domain.service.GifSearchUseCase
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class GifDomainModule {

    @Provides
    @Reusable
    fun gifService(
        gifRepository: GifRepository
    ): GifSearchUseCase = GifServiceImpl(
        gifRepository = gifRepository
    )

}
