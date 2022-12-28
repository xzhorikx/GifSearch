package alex.zhurkov.gifloader.app.di

import javax.inject.Qualifier
import javax.inject.Scope

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class GiphyHttpClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ImageLoaderHttpClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LoggingInterceptor

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheInterceptor

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class GiphyKeyInterceptor

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class GiphyRenditionInterceptor

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppContext

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope