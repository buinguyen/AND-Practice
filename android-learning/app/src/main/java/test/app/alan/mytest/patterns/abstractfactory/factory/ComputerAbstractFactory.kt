package test.app.alan.mytest.patterns.abstractfactory.factory

import test.app.alan.mytest.patterns.abstractfactory.model.Computer

interface ComputerAbstractFactory {
    fun createComputer(): Computer
}