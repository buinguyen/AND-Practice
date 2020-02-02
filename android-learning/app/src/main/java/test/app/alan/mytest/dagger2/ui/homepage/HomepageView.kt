package test.app.alan.mytest.dagger2.ui.homepage

import test.app.alan.mytest.dagger2.model.WikiHomepage

interface HomepageView {
  fun displayLoading()

  fun dismissLoading()

  fun displayHomepage(result: WikiHomepage)

  fun displayError(error: String?)
}