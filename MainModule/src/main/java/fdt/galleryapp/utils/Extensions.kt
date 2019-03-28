package fdt.galleryapp.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
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

inline val Context.connectivityManager: ConnectivityManager?
    get() = getSystemService(CONNECTIVITY_SERVICE) as? ConnectivityManager
