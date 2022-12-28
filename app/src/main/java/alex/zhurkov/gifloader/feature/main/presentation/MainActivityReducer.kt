package alex.zhurkov.gifloader.feature.main.presentation

import alex.zhurkov.gifloader.common.arch.Reducer

class MainActivityReducer : Reducer<MainActivityState, MainActivityChange> {
    override fun reduce(state: MainActivityState, change: MainActivityChange) = when (change) {
        is MainActivityChange.SearchQueryChanged -> state.copy(
            searchQuery = change.data,
            gifPages = emptyList(),
            offset = 0,
            totalCount = 0
        )
        is MainActivityChange.GifPageLoadingIdChanged -> state.copy(loadingPageId = change.id)
        is MainActivityChange.GifPageLoaded -> {
            state.copy(
                gifPages = state.gifPages + change.data,
                offset = state.offset + change.data.gifs.size,
                totalCount = change.data.totalCount
            )
        }
        is MainActivityChange.LastVisibleGifChanged -> state.copy(lastVisibleGifId = change.id)
    }
}
