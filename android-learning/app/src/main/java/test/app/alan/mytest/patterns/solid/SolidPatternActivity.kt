package test.app.alan.mytest.patterns.solid

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_solid_patterns.*
import test.app.alan.mytest.R
import test.app.alan.mytest.patterns.solid.filter.ColorFilterSpecification
import test.app.alan.mytest.patterns.solid.filter.ProductFilter
import test.app.alan.mytest.patterns.solid.filter.SizeFilterSpecification

class SolidPatternActivity : Activity() {

    private val products = arrayListOf(Product("Product 1", "red", 1),
            Product("Product 2", "red", 2),
            Product("Product 3", "white", 3),
            Product("Product 4", "white", 4),
            Product("Product 5", "white", 5),
            Product("Product 6", "blue", 6),
            Product("Product 7", "blue", 7),
            Product("Product 8", "green", 8),
            Product("Product 9", "green", 9),
            Product("Product 10", "green", 10))

    lateinit var mAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solid_patterns)

        mAdapter = ProductAdapter(products, this)
        rv_products.layoutManager = LinearLayoutManager(this)
        rv_products.adapter = mAdapter

        rg_filter.setOnCheckedChangeListener { radioGroup, id ->
            run {
                Log.d("", "id = " + id)
                when (id) {
                    R.id.rb_color -> {
                        val newProducts = ProductFilter.filterBy(products,
                                ColorFilterSpecification("white"))
                        mAdapter.products = newProducts
                        mAdapter.notifyDataSetChanged()
                    }
                    R.id.rb_size -> {
                        val newProducts = ProductFilter.filterBy(products,
                                SizeFilterSpecification(3))
                        mAdapter.products = newProducts
                        mAdapter.notifyDataSetChanged()
                    }
                    R.id.rb_color_size -> {
                        var colorProducts = ProductFilter.filterBy(products,
                                ColorFilterSpecification("green"))
                        colorProducts = ProductFilter.filterBy(colorProducts,
                                SizeFilterSpecification(9))
                        mAdapter.products = colorProducts
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}
