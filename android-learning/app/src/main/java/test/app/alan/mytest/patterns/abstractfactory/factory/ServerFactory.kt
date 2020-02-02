package test.app.alan.mytest.patterns.abstractfactory.factory

import test.app.alan.mytest.patterns.abstractfactory.model.Computer
import test.app.alan.mytest.patterns.abstractfactory.model.Server

class ServerFactory : ComputerAbstractFactory {

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

    override fun createComputer(): Computer {
        return Server(RAM, HDD, CPU, GPU)
    }
}