package test.app.alan.mytest.dagger2.network

class Homepage(private val api: WikiApi) {
    fun get() = api.getHomepage()
}