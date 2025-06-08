package com.deportes.clubdeportivo.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OptionAdapter(
    private val options: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<OptionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textOption: TextView = itemView.findViewById(android.R.id.text1)

        init {
            itemView.setOnClickListener {
                val selectedOption = options[adapterPosition]
                onClick(selectedOption)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = options.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textOption.text = options[position]
    }
}
