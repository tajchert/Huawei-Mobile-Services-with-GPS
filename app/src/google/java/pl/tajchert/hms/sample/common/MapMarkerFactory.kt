package pl.tajchert.hms.sample.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import pl.tajchert.hms.sample.R
import pl.tajchert.hms.sample.data.Station

class MapMarkerFactory {
    private var markerIcon: BitmapDescriptor? = null

    fun getMarkerForStation(station: Station, context: Context): MarkerOptions? {
        //This could be extracted to ViewModel but as this is tightly coupled to View and Context for now I would keep it here, also it makes migration to HMS much easier
        val stationLatLng = LatLng(
            station.latitude,
            station.longitude
        ) //This is important to create LatLng objects in View layer so it is not spread around in you code
        return MarkerOptions().position(stationLatLng)
            .flat(true)
            .draggable(false)
            .anchor(0.5f, 0.5f)
            .icon(getMarkerIcon(context))
    }

    private fun getMarkerIcon(context: Context): BitmapDescriptor {
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