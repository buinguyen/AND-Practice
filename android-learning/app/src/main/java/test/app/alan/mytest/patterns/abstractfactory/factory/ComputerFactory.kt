package test.app.alan.mytest.patterns.abstractfactory.factory

import test.app.alan.mytest.patterns.abstractfactory.model.Computer

class ComputerFactory {
    companion object {
        fun createComputer(caf: ComputerAbstractFactory) : Computer {
            return caf.createComputer()
        }
    }
}