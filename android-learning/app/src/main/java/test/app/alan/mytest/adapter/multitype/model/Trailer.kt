package test.app.alan.mytest.adapter.multitype.model

class Trailer(var key: String, var name: String) : Comparable<String> {

    var type: String = this.javaClass.name

    override fun compareTo(other: String): Int {
        return other.compareTo(type)
    }
}