package test.app.alan.mytest.adapter.multitype

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.review_list_content.view.*
import kotlinx.android.synthetic.main.title_list_content.view.*
import kotlinx.android.synthetic.main.trailer_list_content.view.*
import test.app.alan.mytest.R
import test.app.alan.mytest.adapter.multitype.model.DetailMovieContract
import test.app.alan.mytest.adapter.multitype.model.Review
import test.app.alan.mytest.adapter.multitype.model.Title
import test.app.alan.mytest.adapter.multitype.model.Trailer
import java.util.*

class DetailMovieAdapter(private val listener: DetailMovieContract.MovieListener) : androidx.recyclerview.widget.RecyclerView.Adapter<DetailMovieAdapter.BaseViewHolder<*>>() {

    private val data: MutableList<Comparable<*>>

    companion object {
        private const val TYPE_TITLE = 0
        private const val TYPE_TRAILER = 1
        private const val TYPE_REVIEW = 2
    }

    init {
        data = ArrayList()
    }

    fun swapData(newData: List<Comparable<*>>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val context = parent.context
        return when (viewType) {
            TYPE_TITLE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.title_list_content, parent, false)
                TitleViewHolder(view)
            }
            TYPE_TRAILER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.trailer_list_content, parent, false)
                TrailerViewHolder(view, listener, data)
            }
            TYPE_REVIEW -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.review_list_content, parent, false)
                ReviewViewHolder(view, listener, data)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = data[position]
        when (holder) {
            is TitleViewHolder -> holder.bind(element as Title)
            is TrailerViewHolder -> holder.bind(element as Trailer)
            is ReviewViewHolder -> holder.bind(element as Review)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val comparable = data[position]
        return when (comparable) {
            is Title -> TYPE_TITLE
            is Trailer -> TYPE_TRAILER
            is Review -> TYPE_REVIEW
            else -> throw IllegalArgumentException("Invalid type of data " + position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    abstract class BaseViewHolder<T>(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    class TitleViewHolder(val view: View) : BaseViewHolder<Title>(view) {

        private val titleTextView = view.title_text_view

        override fun bind(title: Title) {
            titleTextView.text = title.value
        }
    }

    class TrailerViewHolder(val view: View, val listener: DetailMovieContract.MovieListener,
                            val data: List<Comparable<*>>) : BaseViewHolder<Trailer>(view), View.OnClickListener {

        val trailerListView = view.trailer_list_linear_layout
        val titleTextView = view.trailer_title_text_view
        val trailerImageView = view.thumbnail_trailer_image_view

        override fun bind(trailer: Trailer) {
            titleTextView.text = trailer.name
            trailerListView!!.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = adapterPosition

            if (position < 0) {
                return
            }

            val trailer = data[position] as Trailer
            listener.onWatch(trailer, adapterPosition)
        }
    }

    class ReviewViewHolder(val view: View, val listener: DetailMovieContract.MovieListener,
                           val data: List<Comparable<*>>) : BaseViewHolder<Review>(view), View.OnClickListener {
        val reviewListView = view.review_list_linear_layout
        val authorTextView = view.author_text_view
        val reviewTextView = view.review_text_view

        override fun bind(review: Review) {
            authorTextView.text = review.author
            reviewTextView.text = review.content
            reviewListView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = adapterPosition

            if (position < 0) {
                return
            }

            val review = data[position] as Review
            listener.onRead(review, adapterPosition)
        }
    }
}

/*
* Java *
@Override
public void onBindViewHolder(BaseViewHolder holder, int position) {
    Comparable element = data.get(position);
    holder.bind(element);
}
*/