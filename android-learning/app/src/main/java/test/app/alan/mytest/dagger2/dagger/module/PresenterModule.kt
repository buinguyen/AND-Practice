package test.app.alan.mytest.dagger2.dagger.module

import dagger.Module
import dagger.Provides
import test.app.alan.mytest.dagger2.network.Homepage
import test.app.alan.mytest.dagger2.network.Wiki
import test.app.alan.mytest.dagger2.ui.homepage.HomepagePresenter
import test.app.alan.mytest.dagger2.ui.homepage.HomepagePresenterImpl
import test.app.alan.mytest.dagger2.ui.search.EntryPresenter
import test.app.alan.mytest.dagger2.ui.search.EntryPresenterImpl
import javax.inject.Singleton

@Module
class PresenterModule {

    @Provides
    @Singleton
    fun provideHomepagePresenter(homepage: Homepage): HomepagePresenter = HomepagePresenterImpl(homepage)

    @Provides
    @Singleton
    fun provideEntryPresenter(wiki: Wiki): EntryPresenter = EntryPresenterImpl(wiki)
}