package com.example.bettertimetoseeyou
// Paquete donde está esta Activity. Sirve para organizar el código dentro del proyecto.

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.selects.SelectInstance
import kotlin.math.abs
import kotlin.math.sqrt

// ---------------------------------------------------------------------
// ShakeActivity
// ---------------------------------------------------------------------
// - Es una Activity (pantalla) que además implementa SensorEventListener.
// - SensorEventListener obliga a implementar métodos como onSensorChanged.
// - El objetivo principal es:
//      * Leer los datos del acelerómetro.
//      * Calcular cuánta "agitación" tiene el móvil.
//      * Mostrar esa intensidad y un texto descriptivo en pantalla.
// ---------------------------------------------------------------------
class ShakeActivity : AppCompatActivity(), SensorEventListener {

    // TextView que mostrará el valor numérico de la intensidad de agitación.
    // Ej: 0.52, 3.40, etc.
    private lateinit var tvShakeValue : TextView

    // TextView que mostrará un texto indicando el "nivel" de esa agitación.
    // Ej: "quieto", "suave", "medio", "depravado" (muy fuerte).
    private lateinit var tvShakeLevel : TextView

    // SensorManager es el "jefe" de los sensores del dispositivo.
    // A través de él accedemos al acelerómetro, giroscopio, etc.
    private lateinit var sensorManager : SensorManager

    // Referencia al propio sensor de acelerómetro.
    // Puede ser null en caso de que el dispositivo no tenga ese sensor.
    private var accelerometer: Sensor? = null

    // -----------------------------------------------------------------
    // Variables para calcular intensidad de agitación
    // -----------------------------------------------------------------
    // lastAcceleration: valor de la aceleración en la lectura anterior.
    private var lastAcceleration = 0f

    // currentAcceleration: valor de la aceleración en la lectura actual.
    private var currentAcceleration = 0f

    // shakeIntensity: medida de cuánto ha cambiado la aceleración
    // entre una lectura y la siguiente. Esa "diferencia" la usamos
    // como indicador de la fuerza de agitación.
    private var shakeIntensity = 0f


    override fun onCreate(savedInstanceState: Bundle?){

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shake)

        tvShakeValue = findViewById(R.id.tvShakeValue)
        tvShakeLevel = findViewById(R.id.tvShakeLevel)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

    }




    // -----------------------------------------------------------------
    // onSensorChanged
    // -----------------------------------------------------------------
    // Se ejecuta AUTOMÁTICAMENTE cada vez que un sensor registrado
    // (en este caso el acelerómetro) tiene nuevos datos.
    //
    // - 'evento' contiene toda la información de la lectura del sensor:
    //     * tipo de sensor (acelerómetro, etc.)
    //     * valores en los ejes X, Y, Z
    // -----------------------------------------------------------------
    override fun onSensorChanged(evento: SensorEvent) {

        // Comprobamos que el evento proviene del acelerómetro.
        // (Podríamos tener varios sensores activos a la vez.)
        if (evento.sensor.type == Sensor.TYPE_ACCELEROMETER){

            // ---------------------------------------------------------
            // 1) Leemos los valores del acelerómetro en los 3 ejes.
            // ---------------------------------------------------------
            // El acelerómetro devuelve la aceleración en cada eje X, Y, Z.
            // Normalmente incluye la gravedad (~9.8 m/s²) en alguno de ellos.
            val x = evento.values[0]
            val y = evento.values[1]
            val z = evento.values[2]

            // ---------------------------------------------------------
            // 2) Calculamos el módulo de la aceleración total.
            // ---------------------------------------------------------
            // Fórmula del módulo de un vector 3D:
            //      |a| = sqrt(x^2 + y^2 + z^2)
            //
            // Esto nos da una única cifra de “cuánta aceleración hay”
            // combinando los 3 ejes.
            val acceleration = sqrt( x * x + y * y + z * z )

            // ---------------------------------------------------------
            // 3) Guardamos la lectura anterior y la nueva
            // ---------------------------------------------------------
            // Lo que nos interesa no es solo la aceleración en sí,
            // sino cuánto HA CAMBIADO respecto a la lectura anterior.
            lastAcceleration = currentAcceleration
            currentAcceleration = acceleration

            // ---------------------------------------------------------
            // 4) Calculamos la diferencia entre aceleración actual y anterior
            // ---------------------------------------------------------
            val delta = currentAcceleration - lastAcceleration

            // Usamos el valor absoluto, porque nos interesa la magnitud
            // del cambio, no si ha subido o bajado.
            shakeIntensity = abs(delta)

            // ---------------------------------------------------------
            // 5) Mostramos la intensidad numérica en pantalla
            // ---------------------------------------------------------
            // String.format("%.2f", shakeIntensity) → 2 decimales.
            // Ej: 0.56, 3.10, 7.89
            tvShakeValue.text = String.format("%.2f", shakeIntensity)

            // ---------------------------------------------------------
            // 6) Interpretamos la intensidad con un texto descriptivo
            // ---------------------------------------------------------
            // Usamos una expresión 'when' (similar a un switch) para
            // clasificar el nivel según el valor de shakeIntensity.
            val levelText = when {
                // Menos de 1 → casi sin movimiento.
                shakeIntensity < 1f -> "Nivel: quieto"

                // Entre 1 y 3 → se mueve un poco.
                shakeIntensity < 3f -> "Nivel: suave"

                // Entre 3 y 6 → agitación media.
                shakeIntensity < 6f -> "Nivel: medio"

                // Más de 6 → estamos diciendo que es un movimiento muy fuerte.
                else -> "Nivel: depravado"
                // (Aquí podrías cambiar el texto a "fuerte" o "muy fuerte"
                // si quieres algo más neutro para clase.)
            }

            // Asignamos el texto calculado al TextView que muestra el nivel.
            tvShakeLevel.text = levelText

        } // fin del if (es acelerómetro)

    } // fin de onSensorChanged


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }


    override fun onResume() {
       super.onResume()
        accelerometer?.also { sensor ->
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_UI

            )

        }
    }

    override fun onPause(){
        super.onPause()
        sensorManager.unregisterListener(this)
    }



}
