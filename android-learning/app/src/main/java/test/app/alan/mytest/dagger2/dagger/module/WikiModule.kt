package test.app.alan.mytest.dagger2.dagger.module

import dagger.Module
import dagger.Provides
import test.app.alan.mytest.dagger2.network.Homepage
import test.app.alan.mytest.dagger2.network.Wiki
import test.app.alan.mytest.dagger2.network.WikiApi
import javax.inject.Singleton

@Module
class WikiModule {
    @Provides
    @Singleton
    fun provideHomepage(api: WikiApi) = Homepage(api)

    @Provides
    @Singleton
    fun provideWiki(api: WikiApi) = Wiki(api)
}