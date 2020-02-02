package test.app.alan.mytest.adapter.multitype

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_multitype.*
import test.app.alan.mytest.R
import test.app.alan.mytest.adapter.multitype.model.DetailMovieContract
import test.app.alan.mytest.adapter.multitype.model.Review
import test.app.alan.mytest.adapter.multitype.model.Title
import test.app.alan.mytest.adapter.multitype.model.Trailer

class MultiTypeAdapterActivity : Activity(), DetailMovieContract.MovieListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multitype)

        initView()
    }

    private fun initView() {
        val listItems = ArrayList<Comparable<String>>()
        val title = Title("This is Title")
        val trailer = Trailer("Trailer key", "Trailer name")
        val review = Review("Reviewer", "Review content")
        listItems.add(title)
        listItems.add(trailer)
        listItems.add(review)

        recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        val adapter = DetailMovieAdapter(this)
        adapter.swapData(listItems)
        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(this, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL))
    }

    override fun onRead(review: Review, pos: Int) {
        Toast.makeText(applicationContext, "Read", Toast.LENGTH_SHORT).show()
    }

    override fun onWatch(trailer: Trailer, pos: Int) {
        Toast.makeText(applicationContext, "Watch", Toast.LENGTH_SHORT).show()
    }
}