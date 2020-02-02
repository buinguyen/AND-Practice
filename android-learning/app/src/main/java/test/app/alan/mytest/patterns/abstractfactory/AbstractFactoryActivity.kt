package test.app.alan.mytest.patterns.abstractfactory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_abstract_factory.*
import test.app.alan.mytest.R
import test.app.alan.mytest.patterns.abstractfactory.factory.ComputerFactory
import test.app.alan.mytest.patterns.abstractfactory.factory.PCFactory
import test.app.alan.mytest.patterns.abstractfactory.factory.ServerFactory

class AbstractFactoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abstract_factory)

        val pc =
                ComputerFactory.createComputer(PCFactory("2 Gb", "512 Gb", "Core due"))
        tv1.text = pc.getConfiguration()

        val server =
                ComputerFactory.createComputer(ServerFactory("16 Gb", "1 Tb", "Core i9", "NVIDIA 1060"))
        tv2.text = server.getConfiguration()
    }

}