package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.dbData.entity.CountryDrivingLicence
import pl.tapo24.twa.dbData.entity.HoldingDocuments
import java.util.*

class HoldingDocumentsAdapter(
    var items: List<HoldingDocuments>,
    var filterResult: List<HoldingDocuments>
): RecyclerView.Adapter<HoldingDocumentsAdapter.HoldingDocumentsAdapterHolder>(), Filterable {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingDocumentsAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_holding_documents, parent,false)
        return HoldingDocumentsAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return filterResult.size
    }

    override fun onBindViewHolder(holder: HoldingDocumentsAdapterHolder, position: Int) {
        val currentItem = filterResult[position]
        holder.bind(currentItem)
    }

    inner class HoldingDocumentsAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HoldingDocuments) {
            var visible = false
            if (item.remarks.isNotEmpty()) {
                visible = true
            }
            binding.setVariable(BR.data, item)
            binding.setVariable(BR.visibleRemarks, visible)
            binding.executePendingBindings()


        }
    }
    override fun getFilter(): Filter {
        val filter = object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint.isNullOrEmpty()){
                    filterResults.count = items.size
                    filterResults.values = items
                } else {
                    val result = items.filter { element -> element.description.contains(constraint,true)  || element.lawText.contains(constraint,true) }
                    filterResults.count = result.size
                    filterResults.values = result
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterResult = results?.values as List<HoldingDocuments>
                notifyDataSetChanged()

            }

        }
        return filter
    }

}