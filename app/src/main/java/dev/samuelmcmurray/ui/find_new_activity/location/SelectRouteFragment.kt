package dev.samuelmcmurray.ui.find_new_activity.location

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import com.google.maps.android.SphericalUtil
import dev.samuelmcmurray.R
import dev.samuelmcmurray.data.model.Activity
import dev.samuelmcmurray.data.repository.SelectRouteRepository
import dev.samuelmcmurray.ui.main.MainActivity
import dev.samuelmcmurray.utilities.MyCallback
import dev.samuelmcmurray.utilities.directionhelpers.DirectionsParser
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


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

    private lateinit var viewModel: SelectRouteViewModel

    private var selectRouteRepository: SelectRouteRepository = SelectRouteRepository()

    // long list to text list view


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

        viewModel = ViewModelProvider(requireActivity(), defaultViewModelProviderFactory).get(
            SelectRouteViewModel::class.java
        )


        setListView()


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
        Log.d(TAG, "updateMap: $location")
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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID

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

    private fun setListView() {
        if(MainActivity.selectedFilter.isEmpty()){
            val builder = AlertDialog.Builder(context)
            builder.setMessage("No filtered item resulting in an empty list of activities. Go back and choose activities.")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->

                    view?.findNavController()?.navigate(R.id.newActivityFragment)
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
        selectRouteRepository.getActivities(object : MyCallback {
            @SuppressLint("SetTextI18n")
            override fun onCallback(value: ArrayList<Activity>) {
                Log.d(TAG, "onCallback: ${value.size}")

                val filters = MainActivity.selectedFilter

                Log.d(TAG, "setListView: ${value.size}")

                val activities = listRunner(value, MainActivity.selectedFilter)

                // test
                val activityFilter = activities.map { activity -> activity.filter }
                Log.d(TAG, "onCallback: $activityFilter")

                // map out the names of each activity
                val activitiesNameList = activities.map { activity -> activity.name }
                Log.d(TAG, "onCallback: $activitiesNameList")
                val routeArray = activitiesNameList.toTypedArray()

                val routeAdapter: ArrayAdapter<String> =
                    ArrayAdapter(requireContext(), R.layout.route_option_item, routeArray)

                val routeListView = requireView().findViewById<ListView>(R.id.route_options)
                routeListView.adapter = routeAdapter

                routeListView.onItemClickListener =
                    AdapterView.OnItemClickListener { parent, view, position, id ->
                        Log.d(TAG, "onItemClick: $position")
                        val selectedActivity = parent?.getItemAtPosition(position)

                        // display dialog
                        val activityDialog = Dialog(requireContext())
                        activityDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        activityDialog.setContentView(R.layout.route_dialog)

                        // dialog widgets
                        val activityName =
                            activityDialog.findViewById<TextView>(R.id.activity_name_dialog)
                        val distance = activityDialog.findViewById<TextView>(R.id.distance_tv)
                        val drivingTime =
                            activityDialog.findViewById<TextView>(R.id.driving_time_tv)
                        val walkingTime =
                            activityDialog.findViewById<TextView>(R.id.walking_time_tv)
                        val cyclingTime =
                            activityDialog.findViewById<TextView>(R.id.cycling_time_tv)

                        val rating = activityDialog.findViewById<RatingBar>(R.id.activity_rating)

                        activityName.text = activities[position].name
                        val loc1 = Location(LocationManager.GPS_PROVIDER)
                        val loc2 = Location(LocationManager.GPS_PROVIDER)

                        val endLatLng = activities[position].latLng

                        loc1.latitude = place1?.position!!.latitude
                        loc1.longitude = place1?.position!!.longitude

                        loc2.latitude = endLatLng.latitude
                        loc2.longitude = endLatLng.longitude

                        /*
                        distance.text = BigDecimal(
                            SphericalUtil.computeDistanceBetween(
                                place1?.position,
                                endLatLng
                            )
                        ).setScale(2, RoundingMode.HALF_EVEN).toString()*/

                        distance.text = "${loc1.distanceTo(loc2).toString()}KM"

                        // time -- soon


                        rating.rating = activities[position].rating.toFloat()

                        activityDialog.show()

                        if (listLatLng.size == 1) {
                            listLatLng.clear()
                            mMap.clear()

                            mMap.addMarker(place1)
                        }

                        // save selected point (destination)
                        listLatLng.add(activities[position].latLng)

                        place2 =
                            MarkerOptions().position(activities[position].latLng).title("Route end")

                        if (listLatLng.size == 0) {
                            // re add first marker
                            place2?.icon(
                                BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_GREEN
                                )
                            )
                        }
                        mMap.addMarker(place2)

                        val url =
                            place1?.position?.let {
                                getRequestUrl(
                                    it,
                                    activities[position].latLng,
                                    "walking"
                                )
                            }
                        val taskRequestDirections = TaskRequestDirections()
                        taskRequestDirections.execute(url)
                        Snackbar.make(
                            requireView(),
                            "Route: $selectedActivity",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
            }
        })
    }

    fun distance(lat_a: Double, lng_a: Double, lat_b: Double, lng_b: Double): Float {
        val earthRadius = 3958.75
        val latDiff = Math.toRadians((lat_b - lat_a))
        val lngDiff = Math.toRadians((lng_b - lng_a))
        val a = sin(latDiff / 2) * sin(latDiff / 2) +
                cos(Math.toRadians(lat_a.toDouble())) * cos(Math.toRadians(lat_b)) *
                sin(lngDiff / 2) * sin(lngDiff / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distance = earthRadius * c
        val meterConversion = 1609
        return (distance * meterConversion.toFloat()).toFloat()
    }

    /**
     * Takes list of {@param activities } from firebase and {@param filter} list
     * Use filter to stream line activity.filter.forEach()
     * Add activities that contain at least 2 of the items in the parsed filter list to the
     * @listViewActivities is converted to a typedArray and used to inflate the list view --> returned
     */
    private fun listRunner(
        activities: ArrayList<Activity>,
        filter: ArrayList<String>
    ): Array<Activity> {
        val listViewActivities = ArrayList<Activity>()

        for (activity in activities) {
            var counter = 0
            for (item in filter) {
                if (item in activity.filter) {
                    if (counter == (filter.size).div(2)) {
                        listViewActivities.add(activity)
                        break
                    }
                    counter++
                }
            }
        }

        return listViewActivities.toTypedArray()
    }


}