package test.app.alan.mytest.dagger2.ui.search

interface EntryPresenter {
  fun setView(entryView: EntryView)

  fun getEntry(query: String)
}