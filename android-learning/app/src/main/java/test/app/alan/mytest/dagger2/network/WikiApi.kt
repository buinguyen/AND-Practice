package test.app.alan.mytest.dagger2.network

import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class WikiApi constructor(
        private val client: OkHttpClient,
        private val builder: HttpUrl.Builder?) {

    fun search(query: String): Call {
        val urlBuilder = builder
                ?.addQueryParameter("action", "query")
                ?.addQueryParameter("list", "search")
                ?.addQueryParameter("format", "json")
                ?.addQueryParameter("srsearch", query)

        return Request.Builder()
                .url(urlBuilder?.build())
                .get()
                .build()
                .let {
                    client.newCall(it)
                }
    }

    fun getHomepage(): Call {
        val urlBuilder = builder
                ?.addQueryParameter("action", "parse")
                ?.addQueryParameter("page", "Main Page")
                ?.addQueryParameter("format", "json")

        return Request.Builder()
                .url(urlBuilder?.build())
                .get()
                .build()
                .let {
                    client.newCall(it)
                }
    }
}