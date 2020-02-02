package test.app.alan.mytest.adapter.multitype.model

class Title(var value: String) : Comparable<String> {

    var type: String = this.javaClass.name

    override fun compareTo(other: String): Int {
        return other.compareTo(type)
    }
}