package pl.tapo24.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.R
import pl.tapo24.BR
import pl.tapo24.dbData.entity.Towing

class TowingAdapter(
    var items: List<Towing>,
    var filterResult: List<Towing>
):RecyclerView.Adapter<TowingAdapter.TowingDataHolder>(), Filterable {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TowingDataHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_towing,parent,false)
        return TowingDataHolder(binding)
    }

    override fun getItemCount(): Int {
        return filterResult.size
    }

    override fun onBindViewHolder(holder: TowingDataHolder, position: Int) {
        val item: Towing = filterResult[position]
        holder.bind(item)
    }

    inner class TowingDataHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Towing) {
            binding.setVariable(BR.data, item)
            binding.executePendingBindings()
        }
    }

    override fun getFilter(): Filter {
        val filter = object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint.isNullOrEmpty()) {
                    filterResults.count = items.size
                    filterResults.values = items
                } else {
                    val result = items.filter { element-> element.name.contains(constraint, true) }
                    filterResults.count = result.size
                    filterResults.values = result
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterResult = results?.values as List<Towing>
                notifyDataSetChanged()

            }

        }
        return filter
    }
}