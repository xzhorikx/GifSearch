package alex.zhurkov.gifloader.feature.main.model

import java.util.*


sealed class GifItem {
    abstract val id: String

    data class Data(
        override val id: String, val url: String
    ) : GifItem()

    data class Loading(override val id: String = UUID.randomUUID().toString()) : GifItem()
}
