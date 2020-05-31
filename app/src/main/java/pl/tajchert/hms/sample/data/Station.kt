package pl.tajchert.hms.sample.data

/*
 * Some mock station object that is represented on the map, important here is not to use ex. LatLng objects as they are inside com.google.* package
 */
data class Station(val id: Long, val latitude: Double, val longitude: Double)