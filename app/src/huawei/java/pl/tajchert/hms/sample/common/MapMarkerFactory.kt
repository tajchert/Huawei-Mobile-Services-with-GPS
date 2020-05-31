package pl.tajchert.hms.sample.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.huawei.hms.maps.model.BitmapDescriptor
import com.huawei.hms.maps.model.BitmapDescriptorFactory
import pl.tajchert.hms.sample.R

class MapMarkerFactory {
    private var markerIcon: BitmapDescriptor? = null

    fun getMarkerIcon(context: Context): BitmapDescriptor {
        if (markerIcon == null) {
            markerIcon = getMarkerIconFromDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.marker_map_simple
                )!!
            )
        }
        return markerIcon!!
    }


    private fun getMarkerIconFromDrawable(drawable: Drawable): BitmapDescriptor {
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}