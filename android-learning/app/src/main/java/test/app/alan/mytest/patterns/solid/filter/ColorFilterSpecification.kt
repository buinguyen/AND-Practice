package test.app.alan.mytest.patterns.solid.filter

import test.app.alan.mytest.patterns.solid.Product
import java.util.ArrayList

class ColorFilterSpecification(var color : String) : AbstractFilterSpecification() {

    override fun applyFilter(products: ArrayList<Product>): ArrayList<Product> {
        var colorProducts : ArrayList<Product> = ArrayList()
        for (product : Product in products) {
            if (product.color == color) colorProducts.add(product)
        }
        return colorProducts
    }

}