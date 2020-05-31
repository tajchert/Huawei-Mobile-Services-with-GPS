package pl.tajchert.hms.sample.data

import kotlin.random.Random

class StationFactory {


    companion object {
        private const val LAT_MAX = 54.834732
        private const val LAT_MIN = 49.002512
        private const val LNG_MAX = 24.128821
        private const val LNG_MIN = 14.124463

        private fun generateRandomStation(): Station {
            return Station(
                id = Random.nextInt(0, 100000).toLong(),
                latitude = Random.nextDouble(LAT_MIN, LAT_MAX),
                longitude = Random.nextDouble(LNG_MIN, LNG_MAX)
            )
        }

        fun generateRandomStations(stationNumber: Long = 100): List<Station> {
            val mutableStations = mutableListOf<Station>()
            for (i in 1..stationNumber) {
                mutableStations.add(generateRandomStation())
            }
            return mutableStations.toList()
        }
    }
}