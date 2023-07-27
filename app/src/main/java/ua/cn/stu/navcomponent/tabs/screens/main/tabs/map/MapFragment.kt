package ua.cn.stu.navcomponent.tabs.screens.main.tabs.map
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import ua.cn.stu.navcomponent.tabs.R
import ua.cn.stu.navcomponent.tabs.model.marker.Markert
import java.util.jar.Manifest


class MapFragment: Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var database: DatabaseReference
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val uiSettings = map.uiSettings
        uiSettings.isMapToolbarEnabled = true
        // Add markers
        addMarkers()

        // Set map options
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_map, container, false)
        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().reference.child("markers")
        val marker1 = Markert(43.2374, 76.8962, "DDTt", "Автомойка для люксовых авто")
        val marker2 = Markert(43.2163, 76.8776, "Car Wash", "Автомойка с бонусами")
        val marker3 = Markert(43.2368, 76.8615, "Service wash", "Самая дешевая автомойка")
        val marker4 = Markert(43.2556, 76.9456, "Super Wash", "Автомойка с кафе")
        val marker5 = Markert(43.2372, 76.9410, "Super Moika", "Автомойка с кафе")
        val marker6 = Markert(43.2874, 76.8870, "Car Spa", "Автомойка с кафе")
        database.child("markers").child("marker1").setValue(marker1)
        database.child("markers").child("marker2").setValue(marker2)
        database.child("markers").child("marker3").setValue(marker3)
        database.child("markers").child("marker4").setValue(marker4)
        database.child("markers").child("marker5").setValue(marker5)
        database.child("markers").child("marker6").setValue(marker6)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            map = googleMap


            // Add markers from Firebase database
            database = FirebaseDatabase.getInstance().reference.child("markers").child("markers")
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (markerSnapshot in dataSnapshot.children) {
                        val latitude = markerSnapshot.child("latitude").value as Double
                        val longitude = markerSnapshot.child("longitude").value as Double
                        val title = markerSnapshot.child("title").value as String
                        val snippet = markerSnapshot.child("snippet").value as String
                        val marker = MarkerOptions().position(LatLng(latitude, longitude))
                            .title(title)
                            .snippet(snippet)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.carmarker))
                        map.addMarker(marker)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Ошибка чтения из Firebase", error.toException())
                }
            })
            val cameraPosition = CameraPosition.Builder()
                .target(LatLng(43.238949, 76.899709))
                .zoom(12f)
                .build()
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            val uiSettings = map.uiSettings
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isCompassEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
        }
        if (isLocationPermissionGranted()) {
            showCurrentLocation()
        } else {
            requestLocationPermission()
        }
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = resources.getString(R.string.Map)
        return view
    }

    val markers = arrayOf(
            MarkerOptions().position(LatLng(43.23745737813256, 76.89621755193542)).title("DDT").snippet("Автомойка"),
    )
    private fun addMarkers() {
        /*for (marker in markers) {
            val bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.carmarker)
            marker.icon(bitmapDescriptor)
            map.addMarker(marker)
        }*/
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(43.238949, 76.899709))
            .zoom(12f)
            .build()
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        )
            if (isLocationPermissionGranted()) {
                showCurrentLocation()
            } else {
                // Пользователь отклонил запрос на разрешение, обработайте здесь логику
            }
    }
    private fun showCurrentLocation() {
        // Проверяем разрешения на доступ к местоположению
        if (isLocationPermissionGranted()) {

            // Получаем последнее известное местоположение пользователя
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                }
            }
        }
    }
}