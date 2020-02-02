package test.app.alan.mytest.patterns.solid.filter

import test.app.alan.mytest.patterns.solid.Product
import java.util.*

abstract class AbstractFilterSpecification {
    fun filter(products: ArrayList<Product>): ArrayList<Product> {
        return applyFilter(products)
    }

    protected abstract fun applyFilter(products: ArrayList<Product>): ArrayList<Product>
}
