package test.app.alan.mytest.dagger2.ui.homepage

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_homepage.*
import test.app.alan.mytest.R
import test.app.alan.mytest.dagger2.BaseActivity
import test.app.alan.mytest.dagger2.model.WikiHomepage
import test.app.alan.mytest.dagger2.ui.search.SearchActivity
import test.app.alan.mytest.dagger2.utils.errorDialog
import test.app.alan.mytest.dagger2.utils.parseHtml
import test.app.alan.mytest.dagger2.utils.start
import javax.inject.Inject

class HomepageActivity : BaseActivity(), HomepageView {

    @Inject
    lateinit var presenter: HomepagePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        presenter.setView(this)
        presenter.loadHomepage()
    }

    override fun registerInject() {
        getApp().getComponent().inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.homepage, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.search -> {
                    SearchActivity::class.start(this)
                    true
                }

                else -> {
                    super.onOptionsItemSelected(item)
                }
            }

    // Implementation of HomepageView

    override fun displayLoading() {
        wait_progress_bar.post {
            wait_progress_bar.visibility = View.VISIBLE
            homepage_sv.visibility = View.GONE
        }
    }

    override fun dismissLoading() {
        wait_progress_bar.post {
            wait_progress_bar.visibility = View.GONE
            homepage_sv.visibility = View.VISIBLE
        }
    }

    override fun displayHomepage(result: WikiHomepage) {
        homepage_tv.post {
            homepage_tv.text = result.htmlContent.parseHtml()
        }
    }

    override fun displayError(error: String?) {
        Log.e("ERROR", error)
        runOnUiThread {
            R.string.error.errorDialog(this)
        }
    }
}
