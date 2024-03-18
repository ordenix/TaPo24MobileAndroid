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
import pl.tapo24.twa.data.SelectableSearchableDialogElement
import pl.tapo24.twa.dbData.entity.Immunity
import pl.tapo24.twa.ui.road.immunity.immunityPersonList.ImmunityPersonListViewModel
import java.util.*

class ImmunityPersonListAdapter(
    var items: List<Immunity>,
    var viewModel: ImmunityPersonListViewModel
): RecyclerView.Adapter<ImmunityPersonListAdapter.ImmunityPersonListAdapterHolder>(), Filterable {

    private var filterResults: List<Immunity> = items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImmunityPersonListAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_immunity_person, parent,false)
        return ImmunityPersonListAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return filterResults.size
    }

    override fun onBindViewHolder(holder: ImmunityPersonListAdapterHolder, position: Int) {
        val currentItem = filterResults[position]
        holder.bind(currentItem)
    }

    inner class ImmunityPersonListAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Immunity) {
            binding.setVariable(BR.immunity, item)
            binding.setVariable(BR.viewModel, viewModel)
            binding.executePendingBindings()

        }
    }

    override fun getFilter(): Filter {

        val filter = object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (p0.isNullOrEmpty()) {
                    filterResults.count = items.size
                    filterResults.values = items
                } else {
                    val result = items.filter { element -> element.person!!.contains(p0, true) }
                    filterResults.count = result.size
                    filterResults.values = result
                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filterResults = p1?.values as List<Immunity>
                notifyDataSetChanged()
            }
        }
        return filter
    }


}