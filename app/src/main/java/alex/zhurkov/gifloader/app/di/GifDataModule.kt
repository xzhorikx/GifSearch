package alex.zhurkov.gifloader.app.di

import alex.zhurkov.gifloader.data.remote.GifRepositoryImpl
import alex.zhurkov.gifloader.data.remote.GiphyRemoteSource
import alex.zhurkov.gifloader.domain.repository.GifRepository
import dagger.Module
import dagger.Provides

@Module
class GifDataModule {

    @Provides
    fun gifRepository(
        remoteSource: GiphyRemoteSource
    ): GifRepository = GifRepositoryImpl(
        remoteSource = remoteSource
    )

}
