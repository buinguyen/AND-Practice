package test.app.alan.mytest.app

import android.app.Application
import test.app.alan.mytest.dagger2.dagger.component.AppComponent
import test.app.alan.mytest.dagger2.dagger.component.DaggerAppComponent
import test.app.alan.mytest.dagger2.dagger.module.AppModule

class TestApplication : Application() {

    lateinit var wikiComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        wikiComponent = initDagger(this)
    }

    private fun initDagger(app: TestApplication): AppComponent =
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .build()

    fun getComponent(): AppComponent {
        return wikiComponent
    }
}