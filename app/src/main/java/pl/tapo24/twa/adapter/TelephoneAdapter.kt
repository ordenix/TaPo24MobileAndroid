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
import pl.tapo24.twa.dbData.entity.TelephoneNumbers
import pl.tapo24.twa.ui.helpers.telephone.TelephoneViewModel
import java.util.*

class TelephoneAdapter(
    var items: List<TelephoneNumbers>,
    var filterResult: List<TelephoneNumbers>,
    var viewModel: TelephoneViewModel
): RecyclerView.Adapter<TelephoneAdapter.TelephoneAdapterHolder>(), Filterable {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TelephoneAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_telephone_book, parent,false)
        return TelephoneAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return filterResult.size
    }

    override fun onBindViewHolder(holder: TelephoneAdapterHolder, position: Int) {
        val currentItem = filterResult[position]
        holder.bind(currentItem)
    }

    inner class TelephoneAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TelephoneNumbers) {
            binding.setVariable(BR.data, item)
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
                    val result = items.filter { element -> element.name.contains(p0, true)
                            || element.branchName.contains(p0, true)
                            || element.phoneNumber.contains(p0, true) }
                    filterResults.count = result.size
                    filterResults.values = result
                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filterResult = p1?.values as List<TelephoneNumbers>
                notifyDataSetChanged()
            }
        }
        return filter
    }


}