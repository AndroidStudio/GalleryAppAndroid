package fdt.galleryapp.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import fdt.galleryapp.R
import kotlinx.android.synthetic.main.toolbar.*

@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        toolbar.title = getToolbarTitle()
        displayBackButton()
    }

    private fun displayBackButton() {
        if (inBackArrowEnabled()) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }
    }

    open fun inBackArrowEnabled(): Boolean {
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    abstract fun getToolbarTitle(): String

    open fun showErrorMessage(message: String?) {
        val builder: AlertDialog.Builder? = AlertDialog.Builder(this)
        builder?.setMessage(message)
        builder?.setTitle(R.string.message)
        builder?.setCancelable(false)
        builder?.setPositiveButton("OK") { dialog, _ ->
            dialog?.dismiss()
            finish()
        }
        val dialog: AlertDialog? = builder?.create()
        dialog?.show()
    }
}