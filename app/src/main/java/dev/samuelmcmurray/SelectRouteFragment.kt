package dev.samuelmcmurray

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

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

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

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
                mMap.addMarker(
                    MarkerOptions().position(latLng!!).title(locationFromGeo.featureName)
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))

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
}