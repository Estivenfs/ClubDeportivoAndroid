package com.deportes.clubdeportivo

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deportes.clubdeportivo.models.Cliente


class SocioAdapter(private val socios: List<Cliente>) :
    RecyclerView.Adapter<SocioAdapter.SocioViewHolder>() {

    class SocioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvId: TextView = itemView.findViewById(R.id.tvIdSocio)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreSocio)
        val tvDni: TextView = itemView.findViewById(R.id.tvDniSocio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_socio, parent, false)
        return SocioViewHolder(view)
    }

    override fun onBindViewHolder(holder: SocioViewHolder, position: Int) {
        val socio = socios[position]
        holder.tvId.text = socio.idCliente.toString()
        holder.tvNombre.text = socio.nombre
        holder.tvDni.text = socio.dni

        // Añadir el OnClickListener al itemView
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ActualizacionDatosActivity::class.java).apply {
                putExtra("id_socio", socio.idCliente) // Pasa el ID del socio como extra
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = socios.size
}
