package com.example.bettertimetoseeyou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    // -----------------------------
    // onCreate: punto de entrada
    // -----------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView si usas un layout XML. Ejemplo:
        // setContentView(R.layout.activity_main)

        // Puedes llamar aquí a las funciones para probarlas:
        recorreNumeros()
        println("SumaHasta(10): ${sumaHasta(10)}")
        println("SumaHasta2(10): ${sumaHasta2(10)}")
        println("SumaHasta3(10): ${sumaHasta3(10)}")
        println("Tipo número 0: ${tipoNumero(0)}")
    }

    // --------------------------------
    // Función para comprobar si un nº es par
    // --------------------------------
    private fun esPar(n: Int) = n % 2 == 0

    // --------------------------------
    // Función que recorre un array y usa forEach + if
    // --------------------------------
    private fun recorreNumeros() {
        val numeros = arrayOf(3, 5, 5, 6, 7)

        numeros.forEach { num ->
            if (esPar(num)) {
                println("$num es Par")
            } else {
                println("$num es Impar")
            }
        }
    }

    // --------------------------------
    // Suma del 1 hasta n
    // --------------------------------
    private fun sumaHasta(n: Int): Int {
        var total = 0
        for (i in 1..n) {
            total += i
        }
        return total
    }

    // --------------------------------
    // Suma de valores desde 1 hasta n (sin incluir n), saltando de 2 en 2
    // --------------------------------
    private fun sumaHasta2(n: Int): Int {
        var total = 0
        for (i in 1 until n step 2) {
            total += i
        }
        return total
    }

    // --------------------------------
    // Suma desde n hasta 1 en pasos de 2
    // --------------------------------
    private fun sumaHasta3(n: Int): Int {
        var total = 0
        for (i in n downTo 1 step 2) {
            total += i
        }
        return total
    }

    // --------------------------------
    // Clasifica un número usando when
    // --------------------------------
    private fun tipoNumero(n: Int): String {
        return when {
            n < 0 -> "Negativo"
            n == 0 -> "Cero"
            else -> "Positivo"
        }
    }
}
