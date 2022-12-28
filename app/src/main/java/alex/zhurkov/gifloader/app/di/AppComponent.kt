package alex.zhurkov.gifloader.app.di

import alex.zhurkov.gifloader.app.GifApplication
import alex.zhurkov.gifloader.feature.main.di.MainActivityComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        GifDataModule::class,
        ImageLoaderModule::class
    ]
)
interface AppComponent {
    fun inject(target: GifApplication)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: GifApplication): AppComponent
    }

    fun plusMainActivityComponent(): MainActivityComponent.Factory
}