package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.dbData.entity.Law

class PdfAdapter(
    var items: List<Law>
): RecyclerView.Adapter<PdfAdapter.PdfAdapterHolder>() {
    var onItemClick: ((Law) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfAdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_element_law, parent, false)
        return PdfAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PdfAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class PdfAdapterHolder(val view: View): RecyclerView.ViewHolder(view) {
        val container:ConstraintLayout = view.findViewById<ConstraintLayout>(R.id.container)
        fun bind(item: Law) {
            container.setOnClickListener {
                onItemClick?.invoke(item)
            }

        }
    }


}