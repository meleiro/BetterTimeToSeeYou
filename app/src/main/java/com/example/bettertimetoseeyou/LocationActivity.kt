package com.example.bettertimetoseeyou
// Paquete donde se encuentra esta Activity. Sirve para organizar el código
// y debe coincidir con el definido en el AndroidManifest si se usa esta Activity.

/*
    IMPORTS NECESARIOS
    ------------------
    - Manifest: para acceder a las constantes de permisos (ACCESS_FINE_LOCATION, etc.)
    - Context, PackageManager: para interactuar con el sistema Android.
    - Location, LocationListener, LocationManager: para trabajar con el GPS / localización.
    - Bundle: para el ciclo de vida de la Activity.
    - TextView, Toast: componentes visuales.
    - AppCompatActivity: Activity base con compatibilidad hacia atrás.
    - ActivityCompat, ContextCompat: para gestionar permisos en tiempo de ejecución.
*/

import android.Manifest
import android.content.Context
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
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

/*
    LocationActivity
    ----------------
    - Es una Activity que implementa LocationListener.
    - LocationListener obliga a implementar métodos como onLocationChanged, etc.
    - El objetivo principal de esta Activity es:
        * Pedir permiso de localización al usuario.
        * Escuchar actualizaciones del GPS.
        * Mostrar las coordenadas en pantalla.
*/

class LocationActivity : AppCompatActivity(), LocationListener {

    // TextView donde mostraremos la latitud y longitud actuales.
    private lateinit var tvLatLon: TextView

    // TextView donde mostraremos el estado de la localización (esperando, recibida, permiso denegado, etc.)
    private lateinit var tvStatus: TextView

    // LocationManager es el servicio del sistema que permite acceder a los proveedores de localización (GPS, red, etc.).
    private lateinit var locationManager: LocationManager

    // Código numérico que usaremos para identificar la petición de permiso de localización.
    // Este mismo código se comprobará en onRequestPermissionsResult.
    private val locationRequestCode = 1001

    private lateinit var mapView: MapView
    private lateinit var mapMarker: Marker

    // Método de ciclo de vida: se llama cuando la Activity se crea por primera vez.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Definimos qué layout XML va a usar esta Activity.
        setContentView(R.layout.activity_location)

        // Vinculamos los TextView del layout XML con las variables del código usando su ID.
        tvLatLon = findViewById(R.id.tvLatLon)
        tvStatus = findViewById(R.id.tvStatus)

        Configuration.getInstance().userAgentValue = packageName

        // Vinculamos el MapView del layout
        mapView = findViewById(R.id.mapView)

        // Permitimos zoom con dos dedos, etc.
        mapView.setMultiTouchControls(true)

        // Establecemos un zoom inicial
        val mapController = mapView.controller
        mapController.setZoom(16.0)

        // Centramos en una posición inicial (por ejemplo, Madrid)
        val startPoint = GeoPoint(40.4168, -3.7038)
        mapController.setCenter(startPoint)

        // Creamos un marcador inicial
        mapMarker = Marker(mapView)
        mapMarker.position = startPoint
        mapMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mapMarker.title = "Tu posición"
        mapView.overlays.add(mapMarker)


        // Obtenemos el servicio de localización del sistema Android.
        // getSystemService devuelve un objeto genérico, lo convertimos (cast) a LocationManager.
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Iniciamos el proceso de comprobación de permisos y, si se concede, empezamos a recibir localizaciones.
        checkPermissionAndStart()
    }

    /*
        checkPermissionAndStart()
        -------------------------
        - Comprueba si ya tenemos el permiso de localización de alta precisión (ACCESS_FINE_LOCATION).
        - Si NO lo tenemos, lanzamos el diálogo de petición de permiso.
        - Si SÍ lo tenemos, llamamos a startLocationUpdates() para comenzar a recibir actualizaciones.
    */
    private fun checkPermissionAndStart() {
        // Comprobamos si el permiso ha sido concedido.
        val hasPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            // Si NO tenemos permiso, lo solicitamos al usuario.
            // Esta llamada dispara un diálogo del sistema pidiendo el permiso.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), // Lista de permisos a pedir.
                locationRequestCode                               // Código que usaremos para identificar esta petición.
            )
        } else {
            // Si ya lo teníamos concedido, comenzamos directamente las actualizaciones de localización.
            startLocationUpdates()
        }
    }

    /*
        startLocationUpdates()
        ----------------------
        - Se encarga de registrarse para recibir actualizaciones de la localización.
        - Usa LocationManager con el proveedor GPS.
        - IMPORTANTE: sólo se debe llamar si el permiso ya ha sido concedido.
    */

    @Suppress("MissingPermission")  // Indicamos al compilador que sabemos lo que hacemos respecto al permiso.
    private fun startLocationUpdates() {
        // Cambiamos el texto de estado para informar al usuario de que estamos esperando la localización.
        tvStatus.text = "Esperando localización del GPS..."
        // Pedimos actualizaciones al proveedor GPS:
        // - LocationManager.GPS_PROVIDER -> fuente de datos: el GPS del dispositivo.
        // - 2000L -> intervalo mínimo de tiempo entre actualizaciones (2 segundos).
        // - 0f    -> distancia mínima entre actualizaciones (0 metros, cualquier cambio).
        // - this  -> el listener que recibirá las actualizaciones (esta Activity).
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            2000L,   // cada 2 segundos se permite una nueva actualización
            0f,      // sin distancia mínima: cualquier cambio en la posición dispara el callback
            this
        )
    }

    /*
        onLocationChanged()
        -------------------
        - Método del LocationListener.
        - Se ejecuta automáticamente cada vez que llega una nueva localización.
        - Aquí es donde extraemos latitud y longitud y las mostramos en pantalla.
    */
    override fun onLocationChanged(location: Location) {
        val lat = location.latitude
        val lon = location.longitude

        tvLatLon.text = "Lat: $lat\nLon: $lon"
        tvStatus.text = "Localización recibida"
        Toast.makeText(this, "Coordenadas recibidas", Toast.LENGTH_SHORT).show()

        // Convertimos la localización a GeoPoint de osmdroid
        val newPoint = GeoPoint(lat, lon)

        // Movemos el mapa al nuevo punto
        val mapController = mapView.controller
        mapController.setCenter(newPoint)

        // Movemos el marcador a la nueva posición
        mapMarker.position = newPoint

        // Forzamos repintado del mapa
        mapView.invalidate()
    }


    /*
        onRequestPermissionsResult()
        ----------------------------
        - Se llama automáticamente cuando el usuario responde al diálogo de permisos.
        - Aquí comprobamos:
            * Si el requestCode coincide con el de nuestra petición de localización.
            * Si el usuario ha concedido o denegado el permiso.
        - Si lo concedió, volvemos a intentar iniciar las actualizaciones de localización.
        - Si lo denegó, informamos al usuario.
    */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,  // Lista de permisos que se pidieron (aunque normalmente aquí sólo hay 1).
        grantResults: IntArray           // Resultados de cada permiso: GRANTED o DENIED.
    ) {
        // Llamamos al método de la superclase para mantener el comportamiento por defecto.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Comprobamos que el código de petición coincide con el nuestro
        // y que hay al menos un resultado en grantResults.
        if (requestCode == locationRequestCode &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // Si el primer permiso (ACCESS_FINE_LOCATION) fue concedido...
            startLocationUpdates()
        } else {
            // Si el permiso fue denegado, actualizamos el TextView de estado para avisar al usuario.
            tvStatus.text = "Permiso de localización DENEGADO"
        }
    }

    /*
        onDestroy()
        ----------
        - Método de ciclo de vida que se llama cuando la Activity se está destruyendo.
        - Es un buen lugar para liberar recursos, detener servicios, etc.
        - Aquí dejamos de recibir actualizaciones de localización para evitar:
            * Consumo innecesario de batería.
            * Pérdidas de memoria (memory leaks).
    */
    override fun onDestroy() {
        super.onDestroy()
        // Dejamos de escuchar actualizaciones de la localización asociadas a este listener.
        locationManager.removeUpdates(this)
    }
    override fun onResume() {
        super.onResume()
        mapView.onResume()  // Necesario para el mapa
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()   // Necesario para el mapa
    }

}
