package alex.zhurkov.gifloader.domain.mapper

interface Mapper<T, E> {
    fun map(from: T): E
}
