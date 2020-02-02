package test.app.alan.mytest.adapter.multitype.model

class DetailMovieContract {
    interface MovieListener {
        fun onWatch(trailer: Trailer, pos: Int)
        fun onRead(review: Review, pos: Int)
    }
}