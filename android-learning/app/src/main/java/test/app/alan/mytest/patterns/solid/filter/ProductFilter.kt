package test.app.alan.mytest.patterns.solid.filter

import test.app.alan.mytest.patterns.solid.Product

class ProductFilter {
    companion object {
        fun filterBy(products: ArrayList<Product>,
                     filterSpec: AbstractFilterSpecification): ArrayList<Product> {
            return filterSpec.filter(products)
        }
    }
}