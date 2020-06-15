package pl.tajchert.hms.sample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.huawei.hms.location.LocationCallback
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationResult
import com.huawei.hms.location.LocationServices
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapsInitializer
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.LatLng
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
                context?.let { context ->
                    huaweiMap?.addMarker(mapMarkerFactory.getMarkerForStation(station, context))
                }
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
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            println("Last Location") //This is called with null
        }
        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val mLocationCallback: LocationCallback
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                println("New Location") //This is never called
            }
        }
        //Permissions manually added from Settings->Apps->this app ->permissions
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, requireContext().mainLooper)
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
}