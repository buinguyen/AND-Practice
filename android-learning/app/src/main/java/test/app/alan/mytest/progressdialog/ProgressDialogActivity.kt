package test.app.alan.mytest.progressdialog

import android.app.Activity
import android.os.Bundle
import android.app.ProgressDialog
import test.app.alan.mytest.R

class ProgressDialogActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_dialog)

        showProgressDialog()
    }

    private fun showProgressDialog() {
        val progressDialog = ProgressDialog(this, R.style.MyAlertDialogStyle)
        progressDialog.show()
    }
}
