package com.example.bettertimetoseeyou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bettertimetoseeyou.model.HourWeather

/**
 * MainActivity:
 * - Hereda de AppCompatActivity (Activity con soporte moderno de compatibilidad).
 * - Es la pantalla principal de la app y controla la interfaz definida en activity_main.xml.
 *
 * Objetivos:
 * - Practicar funciones en Kotlin (rangos, when, lambdas, conversiones seguras...).
 * - Manejar eventos de botones, lectura de texto, y paso de datos entre Activities.
 */
class MainActivity : AppCompatActivity() {

    // -------------------------------------------------------------------
    //                   FUNCIONES AUXILIARES DE EJEMPLO
    // -------------------------------------------------------------------

    /** Comprueba si un número es par. */
    private fun esPar(n: Int) = n % 2 == 0

    /** Recorre una lista de números e imprime si cada uno es par o impar. */
    private fun recorreNumeros() {
        val numeros = arrayOf(3, 5, 5, 6, 7)
        numeros.forEach { num ->
            if (esPar(num)) println("$num es Par")
            else println("$num es Impar")
        }
    }

    /** Suma del 1 hasta n (ambos incluidos). */
    private fun sumaHasta(n: Int): Int {
        var total = 0
        for (i in 1..n) total += i
        return total
    }

    /** Suma del 1 hasta n (sin incluir n), saltando de 2 en 2. */
    private fun sumaHasta2(n: Int): Int {
        var total = 0
        for (i in 1 until n step 2) total += i
        return total
    }

    /** Suma desde n hasta 1 bajando de 2 en 2. */
    private fun sumaHasta3(n: Int): Int {
        var total = 0
        for (i in n downTo 1 step 2) total += i
        return total
    }

    /** Clasifica un número como negativo, cero o positivo usando when. */
    private fun tipoNumero(n: Int): String {
        return when {
            n < 0 -> "Negativo"
            n == 0 -> "Cero"
            else -> "Positivo"
        }
    }

    /** Filtra una lista según un criterio booleano (función de orden superior). */
    private fun procesaLista(lista: List<Int>, criterio: (Int) -> Boolean): List<Int> =
        lista.filter(criterio)

    // -------------------------------------------------------------------
    //              CONSTANTES DEL OBJETO COMPANION
    // -------------------------------------------------------------------
    companion object {
        const val TEXTO1 = "TEXTO1"   // Clave fija para enviar texto al Intent
        const val NUMERO1 = 12        // Constante numérica fija (valor entero)
    }

    // -------------------------------------------------------------------
    //                        CICLO DE VIDA / UI
    // -------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vincula esta Activity con su layout XML
        setContentView(R.layout.activity_main)

        // Referencias a los elementos de la interfaz
        val tvTitulo     = findViewById<TextView>(R.id.tvTitulo)
        val etEntrada    = findViewById<EditText>(R.id.etEntrada)
        val btnProcesar  = findViewById<Button>(R.id.btnProcesar)
        val tvResultado  = findViewById<TextView>(R.id.tvResultado)
        val btnIrSegunda = findViewById<Button>(R.id.btnIrSegunda)
        val btnCargar = findViewById<Button>(R.id.btnCargarPrediccion)

        val rv = findViewById<RecyclerView>(R.id.rvHoras)

        // Texto inicial en la parte superior
        tvTitulo.text = "KOTLIN DEMO"


        val adapter = HourWeatherAdapter()

        rv.layoutManager = LinearLayoutManager(this)

        rv.adapter = adapter

        btnCargar.setOnClickListener {

            val datos = listOf(
                HourWeather ("9:00", 18, R.drawable.ic_nublado),
                HourWeather ("10:00", 19, R.drawable.ic_nublado),
                HourWeather ("11:00", 22, R.drawable.ic_sol)

            )
            adapter.submitList(datos)
        }






        // ---------------------------------------------------------------
        // BOTÓN: Ir a la segunda Activity
        // Envía el texto introducido + la constante NUMERO1
        // ---------------------------------------------------------------
        btnIrSegunda.setOnClickListener {
            val textoEntrada = etEntrada.text.toString().trim()
            val numero = textoEntrada.toIntOrNull() // null si no se puede convertir

            // Creamos el Intent explícito hacia SecondActivity
            val intent = Intent(this, SecondActivity::class.java).apply {
                putExtra(TEXTO1, textoEntrada)   // enviamos siempre el texto
                putExtra("NUMERO1", NUMERO1)     // enviamos el número fijo 12
                // También podrías usar una condición: if (numero != null) putExtra(...)
            }
            startActivity(intent)
        }

        // ---------------------------------------------------------------
        // BOTÓN: Procesar texto o número
        // Analiza la entrada y muestra información en el TextView
        // ---------------------------------------------------------------
        btnProcesar.setOnClickListener {
            val valor = etEntrada.text.toString().trim()

            if (valor.isEmpty()) {
                etEntrada.error = "Escribe algo primero"
                tvResultado.text = ""
                return@setOnClickListener
            }

            val num = valor.toIntOrNull()

            val salida = if (num != null) {
                // ---- Caso numérico ----
                buildString {
                    appendLine("Entrada = $num")
                    appendLine("Tipo número = ${tipoNumero(num)}")
                    appendLine("Suma hasta = ${sumaHasta(num)}")
                    appendLine("Es par = ${esPar(num)}")
                }
            } else {
                // ---- Caso texto ----
                buildString {
                    appendLine("Entrada = $valor")
                    appendLine("Longitud = ${valor.length}")
                }
            }

            // Mostramos el resultado
            tvResultado.text = salida
        }

        val btnGPS: Button = findViewById(R.id.btnGPS)

        btnGPS.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)
        }

    }



}
