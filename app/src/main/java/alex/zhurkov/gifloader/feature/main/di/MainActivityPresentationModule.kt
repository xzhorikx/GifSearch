package alex.zhurkov.gifloader.feature.main.di

import alex.zhurkov.gifloader.app.di.ActivityScope
import alex.zhurkov.gifloader.common.arch.Reducer
import alex.zhurkov.gifloader.common.arch.StateToModelMapper
import alex.zhurkov.gifloader.domain.service.GifSearchUseCase
import alex.zhurkov.gifloader.feature.main.presentation.*
import dagger.Module
import dagger.Provides

@Module
class MainActivityPresentationModule {

    @Provides
    @ActivityScope
    fun reducer(): Reducer<MainActivityState, MainActivityChange> = MainActivityReducer()

    @Provides
    @ActivityScope
    fun stateToModelMapper(): StateToModelMapper<MainActivityState, MainActivityModel> =
        MainStateToModelMapper()

    @Provides
    @ActivityScope
    fun viewModelFactory(
        gifSearchUseCase: GifSearchUseCase,
        reducer: Reducer<MainActivityState, MainActivityChange>,
        stateToModelMapper: StateToModelMapper<MainActivityState, MainActivityModel>
    ) = MainActivityViewModelFactory(
        gifSearchUseCase = gifSearchUseCase,
        reducer = reducer,
        stateToModelMapper = stateToModelMapper
    )
}
