package fdt.galleryapp

import android.content.Context
import android.content.Intent
import android.os.Bundle

fun <T> Context.launchActivity(it: Class<T>, init: Intent.() -> Unit = {}, transitionsBundle: Bundle?) {
    val intent = Intent(this, it)
    intent.init()

    if (transitionsBundle != null) {
        startActivity(intent, transitionsBundle)
    } else {
        startActivity(intent)
    }
}