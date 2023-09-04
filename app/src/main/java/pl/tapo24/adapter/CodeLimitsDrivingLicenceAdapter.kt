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
import pl.tapo24.dbData.entity.CodeLimitsDrivingLicence
import pl.tapo24.dbData.entity.ControlList
import pl.tapo24.ui.road.codeLimitsDrivingLicence.CodeLimitsDrivingLicenceViewModel

class CodeLimitsDrivingLicenceAdapter(
    var items: List<CodeLimitsDrivingLicence>,
    var filterResult: List<CodeLimitsDrivingLicence>,
    var viewModel: CodeLimitsDrivingLicenceViewModel
): RecyclerView.Adapter<CodeLimitsDrivingLicenceAdapter.CodeLimitsDrivingLicenceAdapterHolder>(), Filterable {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CodeLimitsDrivingLicenceAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_code_limits_driving_licence, parent,false)
        return CodeLimitsDrivingLicenceAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return filterResult.size
    }

    override fun onBindViewHolder(holder: CodeLimitsDrivingLicenceAdapterHolder, position: Int) {
        val currentItem = filterResult[position]
        holder.bind(currentItem)
    }

    inner class CodeLimitsDrivingLicenceAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CodeLimitsDrivingLicence) {
            binding.setVariable(BR.data,item)
            binding.setVariable(BR.viewModel, viewModel)
            binding.executePendingBindings()


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
                    val result = items.filter { element -> element.code.contains(constraint,true) || element.description.contains(constraint,true)}
                    filterResults.count = result.size
                    filterResults.values = result
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    filterResult = results?.values as List<CodeLimitsDrivingLicence>
                    notifyDataSetChanged()

            }
        }
        return filter
    }


}