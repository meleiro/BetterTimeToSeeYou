package com.example.bettertimetoseeyou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {


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

    private fun procesaLista(lista: List<Int>, criterio: (Int) -> Boolean) : List<Int> =
        lista.filter(criterio)



    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvTitulo = findViewById<TextView>(R.id.tvTitulo)
        val etEntrada = findViewById<EditText>(R.id.etEntrada)
        val btnProcesar = findViewById<Button>(R.id.btnProcesar)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        tvTitulo.text = "KOTLIN DEMO"






    }



}
