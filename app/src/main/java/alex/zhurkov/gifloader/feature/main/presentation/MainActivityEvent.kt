package alex.zhurkov.gifloader.feature.main.presentation

import alex.zhurkov.gifloader.common.arch.UIEvent

sealed class MainActivityEvent : UIEvent {
    data class DisplayError(val error: Throwable) : MainActivityEvent()
}