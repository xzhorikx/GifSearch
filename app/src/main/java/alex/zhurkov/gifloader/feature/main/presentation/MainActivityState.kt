package alex.zhurkov.gifloader.feature.main.presentation

import alex.zhurkov.gifloader.common.arch.UIState
import alex.zhurkov.gifloader.domain.model.GifPage

data class MainActivityState(
    val searchQuery: String,
    val gifPages: List<GifPage>,
    val offset: Int,
    val totalCount: Int,
    val loadingPageId: String?,
    val lastVisibleGifId: String?
) : UIState {
    companion object {
        val EMPTY = MainActivityState(
            searchQuery = "",
            gifPages = emptyList(),
            offset = 0,
            totalCount = 0,
            loadingPageId = null,
            lastVisibleGifId = null
        )
    }

    fun isPageLoaded(id: String) = gifPages.any { it.id == id }
    fun hasMorePages() = offset == 0 || offset < totalCount
    fun lastGifId() = gifPages.flatMap { it.gifs }.map { it.id }.lastOrNull()
}
