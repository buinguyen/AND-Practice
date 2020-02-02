package test.app.alan.mytest.patterns.solid.filter

import test.app.alan.mytest.patterns.solid.Product
import java.util.ArrayList

class SizeFilterSpecification(var size : Int) : AbstractFilterSpecification() {
    override fun applyFilter(products: ArrayList<Product>): ArrayList<Product> {
        var sizeProducts : ArrayList<Product> = ArrayList()
        for (product : Product in products) {
            if (product.size >= size) sizeProducts.add(product)
        }
        return sizeProducts
    }
}