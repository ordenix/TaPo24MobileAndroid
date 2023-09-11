package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.dbData.entity.Law


class PdfAdapter(
    var items: List<Law>,
    var filterResult: List<Law>
): RecyclerView.Adapter<PdfAdapter.PdfAdapterHolder>(), Filterable {
    var onItemClick: ((Law) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfAdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_element_law, parent, false)
        return PdfAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        return filterResult.size
    }

    override fun onBindViewHolder(holder: PdfAdapterHolder, position: Int) {
        val currentItem = filterResult[position]
        holder.bind(currentItem)
    }

    inner class PdfAdapterHolder(val view: View): RecyclerView.ViewHolder(view) {
        val container:ConstraintLayout = view.findViewById<ConstraintLayout>(R.id.container)
        val name:TextView = view.findViewById<TextView>(R.id.pdfName)
        private val pdfIcon:ImageView = view.findViewById<ImageView>(R.id.pdfIcon)

        fun bind(item: Law) {
            name.text = item.name

            val image = view.context.resources.getIdentifier(item.icon,"drawable",view.context.packageName)
            pdfIcon.setImageResource(image)


            container.setOnClickListener {
                onItemClick?.invoke(item)
            }

        }
    }

    override fun getFilter(): Filter {
        val filter = object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint.isNullOrEmpty()){
                    filterResults.count = items.size
                    filterResults.values = items
                } else {
                    var result = items.filter { element -> element.name?.contains(constraint,true) == true  || element.alias?.contains(constraint,true) == true || element.shortName?.contains(constraint,true) == true}
                    filterResults.values = result
                    filterResults.count = result.size
                    return filterResults
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterResult = results?.values as List<Law>
                notifyDataSetChanged()

            }

        }
        return filter
    }


}