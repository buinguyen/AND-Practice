package test.app.alan.mytest.dagger2.ui.homepage

interface HomepagePresenter {
  fun setView(homepageView: HomepageView)

  fun loadHomepage()
}