package test.app.alan.mytest.dagger2.ui.homepage

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import test.app.alan.mytest.dagger2.model.HomepageResult
import test.app.alan.mytest.dagger2.network.Homepage
import java.io.IOException

class HomepagePresenterImpl constructor(private val homepage: Homepage) : HomepagePresenter {

    private lateinit var homepageView: HomepageView

    override fun setView(homepageView: HomepageView) {
        this.homepageView = homepageView
    }

    override fun loadHomepage() {
        homepageView.displayLoading()
        homepage.get().enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                homepageView.dismissLoading()
                if (response?.isSuccessful == true) {
                    response.let {
                        HomepageResult(it).homepage?.let {
                            homepageView.displayHomepage(it)
                        } ?: run {
                            homepageView.displayError(response.message())
                        }
                    }
                } else {
                    homepageView.displayError(response?.message())
                }
            }

            override fun onFailure(call: Call?, t: IOException?) {
                homepageView.displayError(t?.message)
                t?.printStackTrace()
            }
        })
    }
}