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
import pl.tapo24.twa.dbData.entity.Company
import pl.tapo24.twa.ui.helpers.company.CompanyViewModel
import java.util.*

class CompanyAdapter(
    var items: List<Company>,
    var filterResults: List<Company> = items,
    var viewModel: CompanyViewModel
): RecyclerView.Adapter<CompanyAdapter.CompanyAdapterHolder>(), Filterable {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_company, parent,false)
        return CompanyAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return filterResults.size
    }

    override fun onBindViewHolder(holder: CompanyAdapterHolder, position: Int) {
        val currentItem = filterResults[position]
        holder.bind(currentItem)
    }

    inner class CompanyAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Company) {
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
                    val result = items.filter { element -> element.name?.contains(p0, true) == true
                            || element.regonNumber?.contains(p0, true) == true
                            || element.nipNumber?.contains(p0, true) == true
                            || element.adress?.contains(p0, true) == true }
                    filterResults.count = result.size
                    filterResults.values = result
                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filterResults = p1?.values as List<Company>
                notifyDataSetChanged()
            }
        }
        return filter
    }


}