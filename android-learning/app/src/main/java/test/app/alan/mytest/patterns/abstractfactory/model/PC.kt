package test.app.alan.mytest.patterns.abstractfactory.model

class PC : Computer {

    private var RAM: String = ""
    private var HDD: String = ""
    private var CPU: String = ""

    constructor(RAM: String, HDD: String, CPU: String) {
        this.RAM = RAM
        this.HDD = HDD
        this.CPU = CPU
    }

    fun getRAM(): String {
        return this.RAM
    }

    fun getHDD(): String {
        return this.HDD
    }

    fun getCPU(): String {
        return this.CPU
    }

    override fun getConfiguration(): String {
        return "$RAM | $HDD | $CPU"
    }
}