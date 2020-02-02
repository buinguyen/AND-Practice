package test.app.alan.mytest.dagger2.dagger.module

import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import test.app.alan.mytest.dagger2.network.WikiApi
import test.app.alan.mytest.dagger2.utils.Const
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {
    companion object {
        private const val NAME_BASE_URL = "NAME_BASE_URL"
    }

    @Provides
    @Named(NAME_BASE_URL)
    fun provideBaseUrlString(): String = "${Const.PROTOCOL}://${Const.LANGUAGE}.${Const.BASE_URL}"

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Provides
    @Singleton
    fun provideRequestBuilder(@Named(NAME_BASE_URL) baseUrl: String) =
            HttpUrl.parse(baseUrl)?.newBuilder()

    @Provides
    @Singleton
    fun provideWikiApi(client: OkHttpClient, requestBuilder: HttpUrl.Builder?): WikiApi {
        return WikiApi(client, requestBuilder)
    }
}