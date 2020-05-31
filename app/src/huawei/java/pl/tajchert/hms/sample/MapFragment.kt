package pl.tajchert.hms.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapsInitializer
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MarkerOptions
import kotlinx.android.synthetic.huawei.map_fragment.*
import pl.tajchert.hms.sample.common.MapMarkerFactory
import pl.tajchert.hms.sample.data.Station
import pl.tajchert.hms.sample.databinding.MapFragmentBinding


class MapFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = MapFragment()
    }

    private val viewModel: MapViewModel by viewModels()

    private var huaweiMap: HuaweiMap? = null
    private val mapMarkerFactory = MapMarkerFactory()

    private val changeObserver = Observer<List<Station>> { stationList ->
        stationList?.let {
            println("StationCount ${stationList.count()}")
            huaweiMap?.clear()
            stationList.forEach { station ->
                huaweiMap?.addMarker(getMarkerForStation(station))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = MapFragmentBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragment = this
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        this.activity?.let { MapsInitializer.initialize(it) }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun onClickRandomized(v: View) {
        viewModel.getRandomStations()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        mapView.onSaveInstanceState(savedInstanceState)
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
        super.onLowMemory()
    }

    override fun onMapReady(hMap: HuaweiMap) {
        println("Map ready")
        huaweiMap = hMap
        huaweiMap?.uiSettings?.isZoomControlsEnabled = true

        //Restore last know map position
        huaweiMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    viewModel.lastMapLocationLat,
                    viewModel.lastMapLocationLng
                ), viewModel.lastMapLocationZoom
            )
        )
        //Save current position to recreate map in same place
        huaweiMap?.setOnCameraIdleListener {
            huaweiMap?.cameraPosition?.let {
                viewModel.lastMapLocationLat = it.target.latitude
                viewModel.lastMapLocationLng = it.target.longitude
                viewModel.lastMapLocationZoom = it.zoom
            }
        }
        viewModel.liveStations.observe(viewLifecycleOwner, changeObserver)
    }

    private fun getMarkerForStation(station: Station): MarkerOptions? {
        //This could be extracted to ViewModel but as this is tightly coupled to View and Context for now I would keep it here, also it makes migration to HMS much easier
        val stationLatLng = LatLng(
            station.latitude,
            station.longitude
        ) //This is important to create LatLng objects in View layer so it is not spread around in you code
        context?.let { context ->
            return MarkerOptions().position(stationLatLng)
                .flat(true)
                .draggable(false)
                .anchor(0.5f, 0.5f)
                .icon(mapMarkerFactory.getMarkerIcon(context))
        }
        return null
    }

}