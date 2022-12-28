package alex.zhurkov.gifloader.feature.main.presentation

import alex.zhurkov.gifloader.common.arch.UIStateChange
import alex.zhurkov.gifloader.domain.model.GifPage

sealed class MainActivityChange : UIStateChange {
    data class SearchQueryChanged(val data: String) : MainActivityChange()
    data class GifPageLoadingIdChanged(val id: String?) : MainActivityChange()
    data class GifPageLoaded(val data: GifPage) : MainActivityChange()
    data class LastVisibleGifChanged(val id: String?) : MainActivityChange()
}
