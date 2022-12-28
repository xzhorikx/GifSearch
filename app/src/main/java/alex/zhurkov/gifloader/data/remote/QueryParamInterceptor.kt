package alex.zhurkov.gifloader.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class QueryParamInterceptor(
    private val param: Pair<String, String>
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val modifiedUrl = original.url
            .newBuilder()
            .addQueryParameter(param.first, param.second)
            .build()
        val modifiedRequest = original
            .newBuilder()
            .url(modifiedUrl)
            .build()
        return chain.proceed(modifiedRequest)
    }
}