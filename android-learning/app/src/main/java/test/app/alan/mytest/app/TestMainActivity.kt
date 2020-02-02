package test.app.alan.mytest.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import test.app.alan.mytest.R
import test.app.alan.mytest.adapter.multitype.MultiTypeAdapterActivity
import test.app.alan.mytest.dagger2.ui.homepage.HomepageActivity
import test.app.alan.mytest.localize.LocalizeActivity
import test.app.alan.mytest.patterns.abstractfactory.AbstractFactoryActivity
import test.app.alan.mytest.patterns.solid.SolidPatternActivity
import test.app.alan.mytest.progressdialog.ProgressDialogActivity

class TestMainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUi()
    }

    private fun initUi() {
        btn_pattern.setOnClickListener { goToSolidPattern() }
        btn_progress_dialog.setOnClickListener { goToProgressDialog() }
        btn_multitype_adapter.setOnClickListener { goToMultitypeAdapter() }
        btn_localize.setOnClickListener{goToLocalize()}
        btn_dagger.setOnClickListener{goToDaggerActivity()}
        btn_abstract_factory.setOnClickListener { goToAbstractFactory() }
    }

    private fun goToSolidPattern() {
        startActivity(Intent(this.applicationContext, SolidPatternActivity::class.java))
    }

    private fun goToProgressDialog() {
        startActivity(Intent(this.applicationContext, ProgressDialogActivity::class.java))
    }

    private fun goToMultitypeAdapter() {
        startActivity(Intent(this.applicationContext, MultiTypeAdapterActivity::class.java))
    }

    private fun goToLocalize() {
        startActivity(Intent(this.applicationContext, LocalizeActivity::class.java))
    }

    private fun goToDaggerActivity() {
        startActivity(Intent(this.applicationContext, HomepageActivity::class.java))
    }

    private fun goToAbstractFactory() {
        startActivity(Intent(this.applicationContext, AbstractFactoryActivity::class.java))
    }
}