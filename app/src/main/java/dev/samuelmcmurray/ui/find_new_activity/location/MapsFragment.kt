package dev.samuelmcmurray.ui.find_new_activity.location

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.snackbar.Snackbar
import dev.samuelmcmurray.R

private const val AUTOCOMPLETE_REQUEST_CODE = 100
private const val REQUEST_CODE = 101

private const val TAG = "MapsFragment"

class MapsFragment : Fragment(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    // private val apiKey = getString(R.string.places_api)

    private var geocode: Geocoder? = null
    private var address = listOf<Address>()
    private var latLng: LatLng? = null

    // get current position for default map marker/location
    private var currentLocation: Location? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var seekBar: SeekBar? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)*/

        Places.initialize(requireContext(), "AIzaSyD1hxjN-ALgPdWmeSflMi5-rpDCjO8gmwg")

        /// Can be used to fetch photos, not sure how currently
        val placesClient: PlacesClient = Places.createClient(this.requireContext())
        geocode = Geocoder(requireContext())
        val autocomplete = view.findViewById<EditText>(R.id.autocomplete_fragment)
        seekBar = requireView().findViewById<SeekBar>(R.id.appCompatSeekBar)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fetchLastLocation()

        autocomplete.setOnClickListener {
            val list: List<Place.Field> =
                listOf(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ID, Place.Field.PHOTO_METADATAS)

            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, list)
                .build(requireContext())

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }


    }

    private fun fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                listOf<String>(Manifest.permission.ACCESS_FINE_LOCATION).toTypedArray(),
                REQUEST_CODE
            )

            return
        }
        val task: Task<Location> = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { p0 ->
            if (p0 != null) {
                currentLocation = p0
                Toast.makeText(
                    requireContext(),
                    "${currentLocation?.latitude} - ${currentLocation?.longitude}",
                    Toast.LENGTH_SHORT
                ).show()
                val supportMapFragment =
                    childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                supportMapFragment?.getMapAsync(this@MapsFragment)
            }
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
        val location = com.google.android.gms.maps.model.LatLng(
            currentLocation!!.latitude,
            currentLocation!!.longitude
        )
        mMap?.addMarker(MarkerOptions().position(location).title("Location current"))
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(location))
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17f))
        // radius max = 1000 from seek bar max value
        val circle = mMap?.addCircle(
            CircleOptions().center(location).radius(150.0).strokeColor(
                Color.BLUE
            ).strokeWidth(10.0F).fillColor(Color.TRANSPARENT)
        )
        mMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
        mMap?.uiSettings?.isMyLocationButtonEnabled = true

        // seek bar handling with default location
        seekBar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                Log.d(TAG, "onProgressChanged: $progress")
                if (seekBar != null) {
                    if (circle != null) {
                        circle.radius = seekBar.progress.toDouble()
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //   TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    Toast.makeText(
                        requireContext(),
                        "Distance range: ${seekBar.progress}",
                        Toast.LENGTH_SHORT
                    ).show()
                };
            }

        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation()
                }
            }

        }
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
                        requireView().findViewById<EditText>(R.id.autocomplete_fragment)
                            .setText(location.featureName)
                        latLng = LatLng(location.latitude, location.longitude)

                        // update mMap **all
                        mMap?.addMarker(
                            MarkerOptions().position(latLng!!).title(location.featureName)
                        )
                        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                        mMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
                        mMap?.uiSettings?.isMyLocationButtonEnabled = true

                        // radius max = 1000 from seek bar max value
                        val circle = mMap?.addCircle(
                            CircleOptions().center(latLng).radius(150.0).strokeColor(
                                Color.BLUE
                            ).strokeWidth(10.0F).fillColor(Color.TRANSPARENT)
                        )
                        seekBar?.setOnSeekBarChangeListener(object :
                            SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(
                                seekBar: SeekBar?,
                                progress: Int,
                                fromUser: Boolean
                            ) {
                                Log.d(TAG, "onProgressChanged: $progress")
                                if (seekBar != null) {
                                    if (circle != null) {
                                        circle.radius = seekBar.progress.toDouble()
                                    }
                                }
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                                //   TODO("Not yet implemented")
                            }

                            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                                if (seekBar != null) {
                                    Snackbar.make(requireView(),"Distance range: ${seekBar.progress}",Snackbar.LENGTH_SHORT).show()
                                };
                            }

                        })

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