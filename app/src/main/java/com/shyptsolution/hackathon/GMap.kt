package com.shyptsolution.hackathon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GMap : AppCompatActivity() {
    lateinit var supprtmap: SupportMapFragment
    lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gmap)

        mapView=findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
//        mapView.getMapAsync()

    }
//    fun onMapReady(googleMap: GoogleMap) {
//        val raipur = LatLng(48.8583, 2.2923)
//        googleMap.addMarker(
//            MarkerOptions()
//                .position(raipur)
//                .title("NIT Raipur")
//        )
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(raipur))
//    }
//    override fun onMapReady(map: GoogleMap) {
//        map?.let {
//            googleMap = it
//        }
//        val sydney = LatLng(21.245520, 81.642050)
//        googleMap.addMarker(
//            MarkerOptions()
//                .position(sydney)
//                .title("NIT Raipur CSE Department")
//        )
//        var zoomlevel = 10.0f
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomlevel))
//    }


}