package com.deportes.clubdeportivo // Asegúrate de que este sea el paquete base de tu proyecto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deportes.clubdeportivo.R // Asegúrate de que R se importe correctamente
import com.deportes.clubdeportivo.models.Actividad // Importa tu clase Actividad

class ActividadesAdapter(private val listaActividades: List<Actividad>) :
    RecyclerView.Adapter<ActividadesAdapter.ActividadViewHolder>() {

    // 1. Clase ViewHolder: Contiene las referencias a las vistas de cada ítem.
    //    Esto evita tener que buscar las vistas (findViewById) cada vez que se recicla un ítem.
    class ActividadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvIdActividad: TextView = itemView.findViewById(R.id.tvIdActividad)
        val tvNombreActividad: TextView = itemView.findViewById(R.id.tvNombreActividad)
        val tvPrecioActividad: TextView = itemView.findViewById(R.id.tvPrecioActividad)
        // La ImageView de la flecha no necesita una ID si no vas a interactuar con ella desde el código
        // val imageViewArrow: ImageView = itemView.findViewById(R.id.imageViewArrow) // Si tuviera ID
    }

    // 2. onCreateViewHolder: Crea y devuelve un nuevo ViewHolder.
    //    Este método se llama cuando el RecyclerView necesita una nueva vista para un ítem.
    //    Aquí es donde "inflas" (conviertes de XML a objetos View) tu layout 'item_actividad.xml'.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividad, parent, false)
        return ActividadViewHolder(view)
    }

    // 3. onBindViewHolder: Vincula los datos a las vistas de un ViewHolder existente.
    //    Este método se llama para asociar los datos de una posición específica con un ViewHolder.
    //    Aquí tomas un objeto Actividad de tu lista y lo "muestras" en las TextViews.
    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        val actividad = listaActividades[position] // Obtiene la Actividad en la posición actual

        holder.tvIdActividad.text = actividad.id_actividad.toString() // Convierte Int a String para TextView
        holder.tvNombreActividad.text = actividad.nombre_actividad
        holder.tvPrecioActividad.text = "$${String.format("%.2f", actividad.precio_actividad)}" // Formato de moneda

        // Si tuvieras un click listener para cada ítem, lo configurarías aquí:
        /*
        holder.itemView.setOnClickListener {
            // Lógica cuando se hace clic en la actividad
            // Por ejemplo, iniciar una nueva actividad con los detalles de la actividad
            val context = holder.itemView.context
            val intent = Intent(context, DetalleActividadActivity::class.java)
            intent.putExtra("actividad_id", actividad.id_actividad)
            context.startActivity(intent)
        }
        */
    }

    // 4. getItemCount: Devuelve el número total de ítems en tu lista de datos.
    //    El RecyclerView usa esto para saber cuántos ítems debe mostrar.
    override fun getItemCount(): Int {
        return listaActividades.size
    }
}