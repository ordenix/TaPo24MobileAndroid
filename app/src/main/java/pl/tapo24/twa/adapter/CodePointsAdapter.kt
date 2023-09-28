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
import pl.tapo24.twa.dbData.entity.CodePointsNew
import pl.tapo24.twa.ui.road.codePoints.CodePointsViewModel
import pl.tapo24.twa.utils.RegexTariff
import java.util.*

class CodePointsAdapter(
    var items: List<CodePointsNew>,
    var filterResult: List<CodePointsNew>,
    var viewModel: CodePointsViewModel
): RecyclerView.Adapter<CodePointsAdapter.CodePointsAdapterHolder>(), Filterable {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CodePointsAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_code_points, parent,false)
        return CodePointsAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return filterResult.size
    }

    override fun onBindViewHolder(holder: CodePointsAdapterHolder, position: Int) {
        val currentItem = filterResult[position]
        holder.bind(currentItem, position)
    }

    inner class CodePointsAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CodePointsNew, position: Int) {
            binding.setVariable(BR.data,item)
            binding.setVariable(BR.position,position)
            binding.setVariable(BR.viewModel,viewModel)
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
                    var query = constraint.toString()
                    if (RegexTariff().code(query)) {
                        query = query.replaceRange(1,1,"-")
                    }
                    val result = items.filter { element -> element.id.contains(query, true) || element.description!!.contains(query,true)}
                    filterResults.count = result.size
                    filterResults.values = result
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterResult = results?.values as List<CodePointsNew>
                notifyDataSetChanged()

            }
        }
        return filter
    }

}