package com.example.bettertimetoseeyou
// Paquete donde está esta Activity. Sirve para organizar y agrupar el código
// dentro de la estructura del proyecto Android. Facilita localizar clases y evita
// conflictos de nombres.

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs
import kotlin.math.sqrt

// ---------------------------------------------------------------------
// ShakeActivity
// ---------------------------------------------------------------------
// Esta Activity:
//   - Muestra en pantalla la intensidad con la que se agita el móvil.
//   - Implementa SensorEventListener para poder "escuchar" los datos
//     del acelerómetro y reaccionar cuando cambian.
//   - Procesa los datos X, Y, Z del acelerómetro para calcular cuánto
//     se mueve el dispositivo y clasifica el "nivel" de movimiento.
// ---------------------------------------------------------------------
class ShakeActivity : AppCompatActivity(), SensorEventListener {

    // TextView donde se mostrará la intensidad de la agitación
    // en formato numérico (ej.: 1.25, 3.80…).
    private lateinit var tvShakeValue : TextView

    // TextView donde se muestra un nivel interpretado:
    // "quieto", "suave", "medio", "depravado".
    private lateinit var tvShakeLevel : TextView

    // SensorManager es la clase responsable de gestionar los sensores
    // del dispositivo (acelerómetro, giroscopio, luz, proximidad, etc.).
    private lateinit var sensorManager : SensorManager

    // Objeto que representa el sensor acelerómetro del dispositivo.
    // Puede ser null si el móvil no tiene acelerómetro.
    private var accelerometer: Sensor? = null

    // -----------------------------------------------------------------
    // Variables usadas para calcular la intensidad del movimiento
    // -----------------------------------------------------------------

    // Almacena la aceleración del sensor en la actualización anterior.
    private var lastAcceleration = 0f

    // Almacena la aceleración actual.
    private var currentAcceleration = 0f

    // Valor final calculado de cuánta agitación hay.
    // Lo calculamos como la diferencia entre la aceleración actual
    // y la aceleración anterior.
    private var shakeIntensity = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shake)

        // Vinculación de los TextView definidos en el XML.
        tvShakeValue = findViewById(R.id.tvShakeValue)
        tvShakeLevel = findViewById(R.id.tvShakeLevel)

        // Obtenemos el SensorManager del sistema para acceder a los sensores.
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Obtenemos el acelerómetro del dispositivo.
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Inicializamos los valores de aceleración con la gravedad terrestre (~9.8).
        // Esto sirve para que la primera comparación no dé resultados exagerados.
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
    }

    // -----------------------------------------------------------------
    // onSensorChanged
    // -----------------------------------------------------------------
    // Este método se ejecuta automáticamente cada vez que llega una nueva
    // lectura del sensor registrado.
    //
    // 'evento' contiene:
    //   - Qué sensor disparó el evento.
    //   - Un array de floats con las lecturas en los ejes X, Y, Z.
    // -----------------------------------------------------------------
    override fun onSensorChanged(evento: SensorEvent) {

        // Verificamos que el evento pertenece al acelerómetro.
        if (evento.sensor.type == Sensor.TYPE_ACCELEROMETER){

            // 1) Lectura de los valores del acelerómetro en X, Y, Z.
            val x = evento.values[0]
            val y = evento.values[1]
            val z = evento.values[2]

            // 2) Cálculo de la aceleración total usando el módulo del vector:
            //      |a| = sqrt( x² + y² + z² )
            //
            // Esto convierte tres valores independientes en una sola magnitud.
            val acceleration = sqrt(x * x + y * y + z * z)

            // 3) Guardamos la lectura anterior y la sustituimos por la nueva.
            lastAcceleration = currentAcceleration
            currentAcceleration = acceleration

            // 4) Delta = diferencia entre la lectura actual y la anterior.
            // El valor absoluto sirve para evitar negativos.
            val delta = currentAcceleration - lastAcceleration
            shakeIntensity = abs(delta)

            // 5) Mostramos la intensidad numérica con dos decimales.
            tvShakeValue.text = String.format("%.2f", shakeIntensity)

            // 6) Clasificamos el nivel de agitación según el valor obtenido.
            val levelText = when {
                shakeIntensity < 1f -> "Nivel: quieto"    // Casi sin movimiento
                shakeIntensity < 2f -> "Nivel: suave"     // Movimiento leve
                shakeIntensity < 4f -> "Nivel: medio"     // Movimiento moderado
                else -> "Nivel: depravado"                // Movimiento muy fuerte
            }

            // Mostramos el texto interpretado en pantalla.
            tvShakeLevel.text = levelText
        }
    }

    // -----------------------------------------------------------------
    // onAccuracyChanged
    // -----------------------------------------------------------------
    // Método requerido por SensorEventListener.
    // No lo usamos aquí, pero debe existir.
    // -----------------------------------------------------------------
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }

    // -----------------------------------------------------------------
    // onResume
    // -----------------------------------------------------------------
    // Cuando la Activity vuelve a estar visible:
    //   → Registramos el listener para empezar a recibir datos del sensor.
    // -----------------------------------------------------------------
    override fun onResume() {
        super.onResume()

        accelerometer?.also { sensor ->
            sensorManager.registerListener(
                this,                        // Listener (esta Activity)
                sensor,                      // Sensor a escuchar
                SensorManager.SENSOR_DELAY_UI // Frecuencia adecuada para UI
            )
        }
    }

    // -----------------------------------------------------------------
    // onPause
    // -----------------------------------------------------------------
    // Cuando la Activity deja de estar en primer plano:
    //   → Dejamos de escuchar el sensor.
    // Esto ahorra batería y evita lecturas innecesarias.
    // -----------------------------------------------------------------
    override fun onPause(){
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}
