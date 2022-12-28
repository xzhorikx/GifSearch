package alex.zhurkov.gifloader.feature.main.presentation

import alex.zhurkov.gifloader.common.arch.StateToModelMapper
import alex.zhurkov.gifloader.domain.model.GifPage
import alex.zhurkov.gifloader.feature.main.model.GifItem

class MainStateToModelMapper : StateToModelMapper<MainActivityState, MainActivityModel> {
    override fun mapStateToModel(state: MainActivityState): MainActivityModel {
        val isPageLoading = state.loadingPageId != null
        val isNoResultsLoaded = with(state) {
            isPageLoading.not() && searchQuery.length >= QUERY_MIN_LENGTH && gifPages.isEmpty()
        }
        val mainContentState = when {
            state.searchQuery.length < QUERY_MIN_LENGTH -> MainContentState.WelcomeScreen
            isNoResultsLoaded -> MainContentState.SearchResultEmpty
            else -> MainContentState.Data(
                mapGifs(
                    gifPages = state.gifPages,
                    isPageLoading = isPageLoading
                )
            )
        }
        return MainActivityModel(
            mainContentState = mainContentState
        )
    }

    private fun mapGifs(
        gifPages: List<GifPage>,
        isPageLoading: Boolean
    ): List<GifItem> {
        val gifs = gifPages.flatMap { page ->
            page.gifs.map { GifItem.Data(id = it.id, url = it.url) }
        }
        val loadingIndicators = when (isPageLoading) {
            true -> (0..7).map { GifItem.Loading(id = "loading_$it") }
            false -> emptyList()
        }
        return (gifs + loadingIndicators).distinctBy { it.id }
    }
}
