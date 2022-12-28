package alex.zhurkov.gifloader.common

fun <T> Boolean?.whenTrue(func: () -> T) = if (this == true) func() else null

fun <T> Boolean?.whenFalse(func: () -> T) = if (this == false) func() else null

fun <T> T.singleItemList() = listOf(this)