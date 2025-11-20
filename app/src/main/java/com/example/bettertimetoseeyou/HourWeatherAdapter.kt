package com.example.bettertimetoseeyou
// Paquete donde vive este adapter dentro del proyecto.

// ---------------------------------------------------------------------------
// IMPORTACIONES NECESARIAS PARA EL ADAPTER
// ---------------------------------------------------------------------------
import android.view.LayoutInflater   // Para inflar (convertir) el XML en una View real
import android.view.View             // Clase base para cualquier elemento visual
import android.view.ViewGroup        // Contenedor Padre de las vistas
import android.widget.ImageView      // Para mostrar iconos
import android.widget.TextView       // Para mostrar texto
import androidx.recyclerview.widget.RecyclerView // RecyclerView + ViewHolder
import com.example.bettertimetoseeyou.model.HourWeather // Modelo de datos

// ---------------------------------------------------------------------------
// ADAPTER DEL RECYCLERVIEW
// - items es una lista mutable que contiene las predicciones meteorológicas.
// - RecyclerView.Adapter requiere indicar qué ViewHolder utiliza (VH).
// ---------------------------------------------------------------------------
class HourWeatherAdapter(
    private val items: MutableList<HourWeather> = mutableListOf()
    // Lista que almacena los datos a mostrar. Comienza vacía.
    // Se puede modificar con add(), clear(), etc.
) : RecyclerView.Adapter<HourWeatherAdapter.VH>() {
    // La clase hereda de RecyclerView.Adapter y usa el ViewHolder "VH".

    // -----------------------------------------------------------------------
    // VIEWHOLDER (VH)
    // - Representa una FILA del RecyclerView.
    // - Guarda las referencias a las vistas del layout del ítem (item_hour.xml)
    //   para NO llamar a findViewById repetidamente → mejora el rendimiento.
    // -----------------------------------------------------------------------
    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Accedemos a las vistas del layout item_hour.xml usando sus IDs
        val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)  // Icono del clima
        val tvHour: TextView  = itemView.findViewById(R.id.tvHour)  // Texto con la hora
        val tvTemp: TextView  = itemView.findViewById(R.id.tvTemp)  // Temperatura
    }

    // -----------------------------------------------------------------------
    // onCreateViewHolder
    // - Se llama SOLO cuando el RecyclerView necesita crear una vista nueva.
    // - Infla (convierte) item_hour.xml en una View real.
    // - Crea un ViewHolder (VH) que guardará las vistas internas.
    // -----------------------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        // LayoutInflater: convierte un XML en un objeto View.
        val view = LayoutInflater
            .from(parent.context) // Usamos el contexto del RecyclerView
            .inflate(
                R.layout.activity_hour,  // El layout XML que define UNA FILA
                parent,              // El padre donde vivirá esta vista (RecyclerView)
                false                // false → NO añadir al padre todavía (lo hace RecyclerView)
            )

        // Devolvemos un ViewHolder con la vista ya inflada.
        return VH(view)
    }

    // -----------------------------------------------------------------------
    // onBindViewHolder
    // - Se llama cada vez que una fila debe "dibujarse" con datos reales.
    // - Aquí "pintamos" el modelo HourWeather en la vista.
    // - holder → contiene las vistas (ImageView y TextViews)
    // - position → índice del elemento en la lista
    // -----------------------------------------------------------------------
    override fun onBindViewHolder(holder: VH, position: Int) {

        // Obtenemos el objeto HourWeather de esa posición
        val item = items[position]

        // Actualizamos la fila con los datos
        holder.ivIcon.setImageResource(item.iconRes)    // Icono del clima
        holder.tvHour.text = item.hour                  // La hora (ej: "12:00")
        holder.tvTemp.text = "${item.tempC}ºC"          // Temperatura con símbolo
    }


    override  fun getItemCount(): Int = items.size


    fun submitList(newItems: List<HourWeather>){
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }


}
