package test.app.alan.mytest.patterns.solid

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.pattern_product_item.view.*
import test.app.alan.mytest.R

class ProductAdapter(var products: ArrayList<Product>,
                     private val context: Context) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun getItemCount(): Int {
        Log.d("", "SIZE = " + products.size)
        return products.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.pattern_product_item, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product: Product = products.get(position)
        holder.fillData(product)
    }

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvName = view.tv_name!!
        var tvColor = view.tv_color!!
        var tvSize = view.tv_size!!

        fun fillData(product: Product?) {
            if (product == null) {
                tvName.text = "N/A"
                tvColor.text = "N/A"
                tvSize.text = "N/A"
            } else {
                tvName.text = product.name
                tvColor.text = product.color
                tvSize.text = product.size.toString()
            }
        }
    }
}