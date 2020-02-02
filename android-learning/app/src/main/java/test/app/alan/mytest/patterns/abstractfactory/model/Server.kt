package test.app.alan.mytest.patterns.abstractfactory.model

class Server : Computer {

    private var RAM: String = ""
    private var HDD: String = ""
    private var CPU: String = ""
    private var GPU: String = ""

    constructor(RAM: String, HDD: String, CPU: String, GPU: String) {
        this.RAM = RAM
        this.HDD = HDD
        this.CPU = CPU
        this.GPU = GPU
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

    fun getGPU(): String {
        return this.GPU
    }

    override fun getConfiguration(): String {
        return "$RAM | $HDD | $CPU | $GPU"
    }
}