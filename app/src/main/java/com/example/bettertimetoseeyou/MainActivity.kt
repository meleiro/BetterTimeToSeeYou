package com.example.bettertimetoseeyou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

/**
 * MainActivity:
 * - Extiende de AppCompatActivity (actividad con soporte de compatibilidad y Material).
 * - Actúa como “pantalla” principal de la app y gestiona la UI declarada en activity_main.xml.
 *
 * Objetivo didáctico:
 * - Practicar funciones en Kotlin, rangos, lambdas, if/when, conversión segura a Int,
 *   manejo básico de la UI (EditText/Button/TextView) y construcción de Strings eficientes.
 */
class MainActivity : AppCompatActivity() {

    // -------------------------------------------------------------------
    //               FUNCIONES DE UTILIDAD (ÁMBITO DE CLASE)
    // -------------------------------------------------------------------

    /**
     * Función “single-expression” (una sola expresión con = ):
     * - Recibe un Int y devuelve un Boolean.
     * - Es inlined por el compilador; muy idiomática en Kotlin para lógica simple.
     * - % calcula el resto; si n % 2 == 0 ⇒ n es par.
     */
    private fun esPar(n: Int) = n % 2 == 0

    /**
     * Recorre un array con forEach (función de orden superior) y usa un if por elemento.
     * - Muestra por Logcat con println (en apps reales se prefiere Log.i/Log.d y/o UI).
     * - arrayOf crea un Array<Int> inmutable en longitud (pero sus elementos pueden cambiar).
     */
    private fun recorreNumeros() {
        val numeros = arrayOf(3, 5, 5, 6, 7) // val: referencia inmutable; el array sigue siendo mutable en contenido.

        // forEach aplica la lambda a cada elemento (num).
        numeros.forEach { num ->
            if (esPar(num)) {
                println("$num es Par")
            } else {
                println("$num es Impar")
            }
        }
    }

    /**
     * Suma del 1 hasta n (incluye n).
     * - Rango "1..n" es inclusivo.
     * - var total: variable mutable (se reasigna).
     * - O( n ) en tiempo; O(1) espacio.
     */
    private fun sumaHasta(n: Int): Int {
        var total = 0
        for (i in 1..n) {      // i toma valores 1,2,3,...,n
            total += i
        }
        return total
    }

    /**
     * Suma valores desde 1 hasta n (excluye n) saltando de 2 en 2.
     * - "until" excluye el límite superior ⇒ 1 until n ⇒ [1, n)
     * - "step 2" avanza de dos en dos: 1,3,5,...
     */
    private fun sumaHasta2(n: Int): Int {
        var total = 0
        for (i in 1 until n step 2) { // i = 1,3,5,...,<n
            total += i
        }
        return total
    }

    /**
     * Suma desde n hasta 1 bajando de 2 en 2.
     * - "downTo" crea un rango descendente.
     * - "step 2" ⇒ n, n-2, n-4, …
     */
    private fun sumaHasta3(n: Int): Int {
        var total = 0
        for (i in n downTo 1 step 2) { // i = n, n-2, ... >=1
            total += i
        }
        return total
    }

    /**
     * Clasifica un número con when (switch potente de Kotlin):
     * - when sin argumento usa condiciones booleanas en cada rama.
     * - Devuelve un String; todas las ramas deben devolver el mismo tipo.
     */
    private fun tipoNumero(n: Int): String {
        return when {
            n < 0 -> "Negativo"
            n == 0 -> "Cero"
            else -> "Positivo"
        }
    }

    /**
     * Función de orden superior:
     * - Recibe una List<Int> y una función (lambda) criterio: (Int) -> Boolean.
     * - Devuelve una nueva lista con los elementos que cumplen el criterio (filter crea copia).
     *
     * Ejemplo de uso:
     *   val pares = procesaLista(listOf(1,2,3,4)) { it % 2 == 0 }
     */
    private fun procesaLista(lista: List<Int>, criterio: (Int) -> Boolean): List<Int> =
        lista.filter(criterio)

    // -------------------------------------------------------------------
    //                        CICLO DE VIDA / UI
    // -------------------------------------------------------------------

    /**
     * onCreate:
     * - Punto de entrada de la Activity (después de instanciarla).
     * - Aquí inflamos el layout, pedimos referencias a las vistas y conectamos listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enlaza esta Activity con res/layout/activity_main.xml
        setContentView(R.layout.activity_main)

        // findViewById: busca vistas por id (si usas ViewBinding/DataBinding esto se evita).
        val tvTitulo    = findViewById<TextView>(R.id.tvTitulo)
        val etEntrada   = findViewById<EditText>(R.id.etEntrada)
        val btnProcesar = findViewById<Button>(R.id.btnProcesar)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        // Texto inicial (ayuda contextual para el alumno/usuario)
        tvTitulo.text = "KOTLIN DEMO"

        // Listener de clic del botón (lambda): se ejecuta cuando el usuario pulsa.
        btnProcesar.setOnClickListener {

            // 1) Leemos el contenido del EditText y eliminamos espacios extremos.
            val valor = etEntrada.text.toString().trim()

            // 2) Validación básica: si está vacío, avisamos y salimos temprano (early return).
            if (valor.isEmpty()) {
                etEntrada.error = "Escribe algo primero" // pinta error visual en el EditText
                tvResultado.text = ""                    // limpia la salida
                return@setOnClickListener               // “label return”: sale solo de la lambda del click
            }

            // 3) Intentamos convertir el texto a Int de forma segura:
            //    - toInt() lanzaría NumberFormatException si no es número.
            //    - toIntOrNull() devuelve null si falla (patrón Kotlin para evitar excepciones de control).
            val num = valor.toIntOrNull()

            // 4) Si num != null, el usuario introdujo un número; si no, tratamos la entrada como texto.
            //    Usamos buildString (internamente usa StringBuilder) por eficiencia al concatenar muchas líneas.
            val salida = if (num != null) {
                // -------------------- CASO NÚMERICO --------------------
                buildString {
                    appendLine("Entrada = $num")
                    appendLine("Tipo número = ${tipoNumero(num)}") // when
                    appendLine("Suma hasta = ${sumaHasta(num)}")   // for + rango inclusivo
                    appendLine("Es par = ${esPar(num)}")           // single-expression function

                }
            } else {
                // -------------------- CASO TEXTO --------------------
                buildString {
                    appendLine("Entrada = $valor")
                    appendLine("Longitud = ${valor.length}") // propiedad length de String


                }
            }

            // 5) Mostramos el resultado final en el TextView inferior.
            tvResultado.text = salida

        }
    }
}
