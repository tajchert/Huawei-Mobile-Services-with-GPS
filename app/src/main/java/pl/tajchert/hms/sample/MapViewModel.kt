package pl.tajchert.hms.sample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.tajchert.hms.sample.data.Station
import pl.tajchert.hms.sample.data.StationFactory

class MapViewModel : ViewModel() {
    /*
     * Our ViewModel should be free from com.google.* dependencies in imports if possible
     */
    val liveStations = MutableLiveData<List<Station>>()

    var lastMapLocationLat: Double = 52.231575
    var lastMapLocationLng: Double = 21.006382
    var lastMapLocationZoom: Float = 7f

    fun getRandomStations() {
        liveStations.value = StationFactory.generateRandomStations(1000)
        //Normally this should be a Repository, however to keep sample as simple as possible it is not
    }
}