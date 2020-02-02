package test.app.alan.mytest.dagger2.network

class Wiki(private val api: WikiApi) {
    fun search(query: String) = api.search(query)
}
