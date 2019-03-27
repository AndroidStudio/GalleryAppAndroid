package fdt.galleryapp.utils.device

import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity

class Device {

    companion object {
        fun getScreenWidth(context :AppCompatActivity) : Int{
            val point = Point()
            context.windowManager.defaultDisplay.getSize(point)
            return point.x
        }
    }
}