package alex.zhurkov.gifloader.feature.main.presentation

import alex.zhurkov.gifloader.common.arch.UIAction

sealed class MainActivityAction : UIAction {
    data class QueryChanged(val data: String) : MainActivityAction()
    data class LastVisibleGifChanged(val id: String?) : MainActivityAction()
}
