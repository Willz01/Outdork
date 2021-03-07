package dev.samuelmcmurray

import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dev.samuelmcmurray.utilities.directionhelpers.DirectionsParser
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


private const val TAG = "SelectRouteFragment"

class SelectRouteFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    private val args: SelectRouteFragmentArgs by navArgs()
    private lateinit var origin: String

    private lateinit var geocode: Geocoder
    private var address: List<Address>? = listOf<Address>()
    private var latLng: LatLng? = null

    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    private var listLatLng = ArrayList<LatLng>()
    private var currentPolyline: Polyline? = null

    private var place1: MarkerOptions? = null
    private var place2: MarkerOptions? = null

    // long list to text list view
    private var routeArray = arrayOf(
        "247 Fitness",
        "ICA maxi",
        "Kristianstad C",
        "ICA Kvantum",
        "247 Fitness",
        "ICA maxi",
        "Kristianstad C",
        "ICA Kvantum",
        "247 Fitness",
        "ICA maxi",
        "Kristianstad C",
        "ICA Kvantum",
        "247 Fitness",
        "ICA maxi",
        "Kristianstad C",
        "ICA Kvantum"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        navController = requireActivity().findNavController(R.id.fragmentContainer)
        bottomNavigationView = requireActivity().findViewById(R.id.nav)
        bottomNavigationView.visibility = View.GONE

        return inflater.inflate(R.layout.fragment_select_route, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this@SelectRouteFragment)


        val routeAdapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), R.layout.route_option_item, routeArray)

        val routeListView = requireView().findViewById<ListView>(R.id.route_options)
        routeListView.adapter = routeAdapter

        routeListView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d(TAG, "onItemClick: $position")
                val selected = parent?.getItemAtPosition(position)
                Snackbar.make(
                    requireView(),
                    "Route: $selected",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

        })


        geocode = Geocoder(requireContext())
        origin = args.origin
    }

    override fun onMapReady(p0: GoogleMap?) {
        if (p0 != null) {
            mMap = p0
        }
        updateMap()
    }


    private fun updateMap() {
        val location: String = origin
        println("*******************$location")
        try {
            address = geocode.getFromLocationName(location, 5) as List<Address>
            if (address!!.isNotEmpty()) {
                val locationFromGeo: Address = address!![0]
                locationFromGeo.latitude
                locationFromGeo.longitude

                latLng = LatLng(locationFromGeo.latitude, locationFromGeo.longitude)

                // update mMap pointer, location (latitude and longitude)
                place1 = MarkerOptions().position(latLng!!).title(locationFromGeo.featureName)
                mMap.addMarker(place1)
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID

                mMap.setOnMapClickListener { latLng1 ->
                    if (listLatLng.size == 1) {
                        listLatLng.clear()
                        mMap.clear()

                        mMap.addMarker(place1)
                    }

                    // save selected point (destination)
                    listLatLng.add(latLng1!!)

                    place2 = MarkerOptions().position(latLng1).title("Route end")

                    if (listLatLng.size == 0) {
                        // re add first marker
                        place2?.icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_GREEN
                            )
                        )
                    }
                    mMap.addMarker(place2)

                    // Direction between markers
                    if (listLatLng.size == 1) {
                        if (place1 == place2){
                            Log.d(TAG, "updateMap: YH, equal")
                        } else {
                            Log.d(TAG, "updateMap: Not Equal")
                        }

                        val url = place1?.position?.let {
                            place2?.position?.let { it1 ->
                                getRequestUrl(
                                    it,
                                    it1, "walking"
                                )
                            }
                        }
                        Log.d(TAG, "updateMap: Map Clicked")
                        val taskRequestDirections = TaskRequestDirections()
                        taskRequestDirections.execute(url)
                        /*FetchURL(requireContext()).execute(url, "walking")
                        MainActivity.mMap = mMap*/
                    }
                }

                println(locationFromGeo)
            } else {
                Snackbar.make(
                    requireView(),
                    "An error occurred",
                    Snackbar.LENGTH_SHORT
                ).show()
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getRequestUrl(origin: LatLng, destination: LatLng, mode: String): String {

        val originValue = "origin=${origin.latitude},${origin.longitude}"
        val desValue = "destination=${destination.latitude},${destination.longitude}"

        val sensor = "sensor=false"
        val modeX = "mode=$mode"

        val key = "key=AIzaSyBVUF4pnYZHIj32SpjqIX22ZZSlxt97zc8"
        val param = "$originValue&$desValue&$sensor&$modeX&$key"
        val outputFormat = "json"
        return "https://maps.googleapis.com/maps/api/directions/$outputFormat?$param"
    }

    private fun requestDirection(requestUrl: String): String {
        var responseMessage: String? = null
        var inputStream: InputStream? = null

        var httpURLConnection: HttpURLConnection? = null
        try {
            val url: URL = URL(requestUrl)
            httpURLConnection = url.openConnection() as HttpURLConnection?
            httpURLConnection?.connect()

            // response

            inputStream = httpURLConnection?.inputStream
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)


            val line = bufferedReader.use { it.readLines().joinToString("") }

            responseMessage = line
            bufferedReader.close()
            inputStreamReader.close()

        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
            httpURLConnection?.disconnect()
        }

        return responseMessage!!
    }

    // will be better in a companion object, but getting required methods and context will be impossible
    inner class TaskRequestDirections :
        AsyncTask<String?, Void?, String>() {
        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            //Parse json here
            val taskParser = TaskParser()
            taskParser.execute(s)
        }

        override fun doInBackground(vararg params: String?): String {
            var responseString = ""
            try {
                responseString = params[0]?.let { requestDirection(it) }.toString()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return responseString
        }
    }

    inner class TaskParser :
        AsyncTask<String?, Void?, List<List<HashMap<String, String>>>?>() {

        override fun onPostExecute(lists: List<List<HashMap<String, String>>>?) {
            //Get list route and display it into the map
            var points: ArrayList<LatLng>? = null
            var polylineOptions: PolylineOptions? = null
            for (path in lists!!) {
                points = ArrayList<LatLng>()
                polylineOptions = PolylineOptions()
                for (point in path) {
                    val lat = point["lat"]!!.toDouble()
                    val lon = point["lon"]!!.toDouble()
                    points.add(LatLng(lat, lon))
                }
                polylineOptions.addAll(points)
                polylineOptions.width(15f)
                polylineOptions.color(Color.GREEN)
                polylineOptions.geodesic(true)
            }
            if (polylineOptions != null) {
                mMap.addPolyline(polylineOptions)
            } else {
                Toast.makeText(requireContext(), "Direction not found!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        override fun doInBackground(vararg params: String?): List<List<HashMap<String, String>>>? {
            var jsonObject: JSONObject? = null
            var routes: List<List<HashMap<String, String>>>? = null
            try {
                jsonObject = JSONObject(params[0]!!)
                val directionsParser = DirectionsParser()
                routes = directionsParser.parse(jsonObject)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return routes
        }
    }

}