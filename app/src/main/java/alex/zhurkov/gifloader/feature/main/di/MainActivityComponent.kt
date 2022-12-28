package alex.zhurkov.gifloader.feature.main.di

import alex.zhurkov.gifloader.app.di.ActivityScope
import alex.zhurkov.gifloader.domain.di.GifDomainModule
import alex.zhurkov.gifloader.feature.main.MainActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [
        GifDomainModule::class,
        MainActivityPresentationModule::class
    ]
)
interface MainActivityComponent {

    fun inject(target: MainActivity)

    interface ComponentProvider {
        fun provideMainComponent(activity: MainActivity): MainActivityComponent
    }

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: MainActivity): MainActivityComponent
    }
}