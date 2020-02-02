package test.app.alan.mytest.patterns.abstractfactory.factory

import test.app.alan.mytest.patterns.abstractfactory.model.Computer
import test.app.alan.mytest.patterns.abstractfactory.model.PC

class PCFactory : ComputerAbstractFactory {

    private var RAM: String = ""
    private var HDD: String = ""
    private var CPU: String = ""

    constructor(RAM: String, HDD: String, CPU: String) {
        this.RAM = RAM
        this.HDD = HDD
        this.CPU = CPU
    }

    override fun createComputer(): Computer {
        return PC(RAM, HDD, CPU)
    }
}