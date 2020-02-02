package test.app.alan.mytest.dagger2.dagger.component

import dagger.Component
import test.app.alan.mytest.dagger2.dagger.module.AppModule
import test.app.alan.mytest.dagger2.dagger.module.NetworkModule
import test.app.alan.mytest.dagger2.dagger.module.PresenterModule
import test.app.alan.mytest.dagger2.dagger.module.WikiModule
import test.app.alan.mytest.dagger2.ui.homepage.HomepageActivity
import test.app.alan.mytest.dagger2.ui.search.SearchActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    PresenterModule::class,
    NetworkModule::class,
    WikiModule::class])
interface AppComponent {

    fun inject(target: HomepageActivity)

    fun inject(target: SearchActivity)
}