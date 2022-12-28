package alex.zhurkov.gifloader.feature.main.presentation

import alex.zhurkov.gifloader.common.arch.Reducer
import alex.zhurkov.gifloader.common.arch.StateToModelMapper
import alex.zhurkov.gifloader.domain.service.GifSearchUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivityViewModelFactory(
    private val gifSearchUseCase: GifSearchUseCase,
    private val reducer: Reducer<MainActivityState, MainActivityChange>,
    private val stateToModelMapper: StateToModelMapper<MainActivityState, MainActivityModel>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MainActivityViewModel(
        gifSearchUseCase = gifSearchUseCase,
        reducer = reducer,
        stateToModelMapper = stateToModelMapper
    ) as T
}
