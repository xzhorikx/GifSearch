package alex.zhurkov.gifloader.app.di

import alex.zhurkov.gifloader.BuildConfig
import alex.zhurkov.gifloader.data.remote.QueryParamInterceptor
import alex.zhurkov.gifloader.data.remote.GiphyRemoteSource
import alex.zhurkov.gifloader.data.remote.interceptor.CacheControlInterceptor
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Reusable
    fun clientBuilder(@AppContext context: Context): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .cache(Cache(context.cacheDir, (5 * 1024 * 1024).toLong())) // 5MB cache
            .callTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)

    @Provides
    @Singleton
    @LoggingInterceptor
    fun loggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    @CacheInterceptor
    fun cacheInterceptor(): Interceptor = CacheControlInterceptor()

    @Provides
    @Singleton
    @GiphyKeyInterceptor
    fun giphyKeyInterceptor(): Interceptor = QueryParamInterceptor(
        param = "api_key" to BuildConfig.GIPHY_API_KEY
    )

    @Provides
    @Singleton
    @GiphyRenditionInterceptor
    fun giphyRenditionInterceptor(): Interceptor = QueryParamInterceptor(
        param = "bundle" to "low_bandwidth"
    )

    @Provides
    @Singleton
    @GiphyHttpClient
    fun giphyHttpClient(
        builder: OkHttpClient.Builder,
        @LoggingInterceptor loggingInterceptor: Interceptor,
        @CacheInterceptor cacheInterceptor: Interceptor,
        @GiphyKeyInterceptor giphyKeyInterceptor: Interceptor,
        @GiphyRenditionInterceptor giphyRenditionInterceptor: Interceptor
    ): OkHttpClient = builder
        .addInterceptor(giphyKeyInterceptor)
        .addInterceptor(cacheInterceptor)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(giphyRenditionInterceptor)
        .build()

    @Provides
    @Singleton
    @ImageLoaderHttpClient
    fun imageLoaderHttpClient(
        builder: OkHttpClient.Builder,
        @LoggingInterceptor loggingInterceptor: Interceptor,
        @CacheInterceptor cacheInterceptor: Interceptor
    ): OkHttpClient = builder
        .addInterceptor(cacheInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun giphyNetworkClient(
        @GiphyHttpClient httpClient: OkHttpClient
    ): GiphyRemoteSource = Retrofit.Builder()
        .baseUrl("https://api.giphy.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(GiphyRemoteSource::class.java)
}
