package alex.zhurkov.gifloader.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class CacheControlInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val cacheSize = 60 * 60 * 24 // 1 day
        val original = chain.request()
        val modifiedHeader = original
            .newBuilder()
            .header("Cache-Control", "public, max-stale=$cacheSize")
            .build()
        return chain.proceed(modifiedHeader)
    }
}