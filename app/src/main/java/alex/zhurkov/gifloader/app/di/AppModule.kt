package alex.zhurkov.gifloader.app.di

import alex.zhurkov.gifloader.app.GifApplication
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    @AppContext
    fun appContext(app: GifApplication): Context = app.applicationContext
}
