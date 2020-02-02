package test.app.alan.mytest.dagger2.ui.search

import test.app.alan.mytest.dagger2.model.Entry

interface EntryView {
  fun displayLoading()

  fun dismissLoading()

  fun displayEntries(results: List<Entry>)

  fun displayError(error: String?)
}