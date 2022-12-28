package alex.zhurkov.gifloader.feature.main.presentation

import alex.zhurkov.gifloader.common.arch.UIModel
import alex.zhurkov.gifloader.feature.main.model.GifItem

data class MainActivityModel(
    val mainContentState: MainContentState
) : UIModel {
    companion object {
        val EMPTY = MainActivityModel(
            mainContentState = MainContentState.WelcomeScreen
        )
    }
}


sealed class MainContentState {
    data class Data(
        val gifItems: List<GifItem>
    ) : MainContentState()

    object WelcomeScreen : MainContentState()
    object SearchResultEmpty : MainContentState()
}