package dev.samuelmcmurray.ui.find_new_activity.location

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import dev.samuelmcmurray.R

private const val AUTOCOMPLETE_REQUEST_CODE = 100
private const val TAG = "MapsFragment"

class MapsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    // private val apiKey = getString(R.string.places_api)

    private var geocode: Geocoder? = null
    private var address = listOf<Address>()
    private var latLng: LatLng? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        Places.initialize(requireContext(), "AIzaSyD1hxjN-ALgPdWmeSflMi5-rpDCjO8gmwg")

        /**
         * Can be used to fetch photos, not sure how currently
         */
        val placesClient: PlacesClient = Places.createClient(this.requireContext())
        geocode = Geocoder(requireContext())
        val autocomplete = view.findViewById<EditText>(R.id.autocomplete_fragment)

        autocomplete.setOnClickListener {
            val list: List<Place.Field> =
                listOf(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ID)

            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, list)
                .build(requireContext())

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // defaults map location, as seen on background
        // Add a marker in Sydney and move the camera
        val sydney = com.google.android.gms.maps.model.LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                FragmentActivity.RESULT_OK -> {
                    val place = data?.let { Autocomplete.getPlaceFromIntent(it) }
                    try {
                        address = geocode?.getFromLocationName(place?.name, 5) as List<Address>

                        val location: Address = address[0]
                        location.latitude
                        location.longitude
                        requireView().findViewById<EditText>(R.id.autocomplete_fragment).setText(location.featureName)
                        latLng = LatLng(location.latitude, location.longitude)

                        // update mMap pointer, location (latitude and longitude)
                        mMap.addMarker(
                            MarkerOptions().position(latLng!!).title(location.featureName)
                        )
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        mMap.uiSettings.isMyLocationButtonEnabled = true


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    Log.i(TAG, "Place: " + place!!.name + ", " + place.id);
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    val status = data?.let { Autocomplete.getStatusFromIntent(it) }
                    status!!.statusMessage?.let { Log.i(TAG, it) }
                }
                else -> Log.i(TAG, "onActivityResult: Cancelled operation")
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)

    }
}