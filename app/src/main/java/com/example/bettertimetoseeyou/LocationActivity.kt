package com.example.bettertimetoseeyou

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LocationActivity : AppCompatActivity(), LocationListener {

    private lateinit var tvLatLon: TextView
    private lateinit var tvStatus: TextView
    private lateinit var locationManager: LocationManager

    private val locationRequestCode = 1001

    // --------------------------------------------------------------------
    // onCreate: se ejecuta al abrir la Activity
    // --------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        // Referencias a las vistas del layout
        tvLatLon = findViewById(R.id.tvLatLon)
        tvStatus = findViewById(R.id.tvStatus)

        // Inicializamos el servicio de localización
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        // Comprobamos permisos y empezamos
        checkPermissionAndStart()
    }

    // --------------------------------------------------------------------
    // Comprueba si tenemos permiso para usar el GPS.
    // Si no, lo solicita. Si sí, empieza las actualizaciones.
    // --------------------------------------------------------------------
    private fun checkPermissionAndStart() {
        val hasPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            // Pedimos permiso al usuario
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationRequestCode
            )
        } else {
            // Ya tenemos permiso → iniciar GPS
            startLocationUpdates()
        }
    }


    // --------------------------------------------------------------------
    // startLocationUpdates: activa el GPS para recibir coordenadas
    // --------------------------------------------------------------------
    @Suppress("MissingPermission")
    private fun startLocationUpdates() {
        tvStatus.text = "Esperando localización..."

        // Pedimos actualizaciones al proveedor de GPS físico
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            2000L,  // cada 2 segundos
            0f,     // sin distancia mínima
            this    // esta Activity implementa LocationListener
        )
    }

    // --------------------------------------------------------------------
    // MÉTODO que se ejecuta cada vez que el GPS recibe nuevas coordenadas
    // --------------------------------------------------------------------


    // --------------------------------------------------------------------
    // Cuando la Activity se destruye, paramos el GPS
    // --------------------------------------------------------------------

}
