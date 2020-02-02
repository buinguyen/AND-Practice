package test.app.alan.mytest.dagger2

import android.app.Activity
import android.os.Bundle
import test.app.alan.mytest.app.TestApplication

abstract class BaseActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerInject()
    }

    fun getApp(): TestApplication {
        return (application as TestApplication)
    }

    abstract fun registerInject()
}