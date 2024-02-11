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
import pl.tapo24.twa.dbData.entity.ControlList
import pl.tapo24.twa.ui.road.controlList.ControlListViewModel

class ControlListAdapter(
    var items: List<ControlList>,
    var filterResult: List<ControlList>,
    var viewModel: ControlListViewModel
): RecyclerView.Adapter<ControlListAdapter.ControlListAdapterHolder>(), Filterable {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ControlListAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_control_list, parent,false)
        return ControlListAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return filterResult.size
    }

    override fun onBindViewHolder(holder: ControlListAdapterHolder, position: Int) {
        val currentItem = filterResult[position]
        holder.bind(currentItem)
    }

    inner class ControlListAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ControlList) {
            binding.setVariable(BR.data, item)
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
                    var result = items.filter { element -> element.subTitleDepth1Name?.contains(constraint,true) == true || element.position?.contains(constraint,true) == true }
                    if (result.isNotEmpty()) {
                        filterResults.values = result
                        filterResults.count = result.size
                        return filterResults
                    }
                    result = items.filter {element ->
                        element.standardNameA1?.contains(constraint,true) == true ||
                        element.standardNameA2?.contains(constraint,true) == true ||
                        element.standardNameA3?.contains(constraint,true) == true ||
                        element.standardNameB1?.contains(constraint,true) == true ||
                        element.standardNameB2?.contains(constraint,true) == true ||
                        element.standardNameB3?.contains(constraint,true) == true ||
                        element.standardNameC1?.contains(constraint,true) == true ||
                        element.standardNameC2?.contains(constraint,true) == true ||
                        element.standardNameC3?.contains(constraint,true) == true ||
                        element.standardNameD1?.contains(constraint,true) == true ||
                        element.standardNameD2?.contains(constraint,true) == true ||
                        element.standardNameD3?.contains(constraint,true) == true ||
                        element.standardNameE1?.contains(constraint,true) == true ||
                        element.standardNameE2?.contains(constraint,true) == true ||
                        element.standardNameE3?.contains(constraint,true) == true ||
                        element.standardNameF1?.contains(constraint,true) == true ||
                        element.standardNameF2?.contains(constraint,true) == true ||
                        element.standardNameF3?.contains(constraint,true) == true ||
                        element.standardNameG1?.contains(constraint,true) == true ||
                        element.standardNameG2?.contains(constraint,true) == true ||
                        element.standardNameG3?.contains(constraint,true) == true ||
                        element.standardNameH1?.contains(constraint,true) == true ||
                        element.standardNameH2?.contains(constraint,true) == true ||
                        element.standardNameH3?.contains(constraint,true) == true ||
                        element.standardNameI1?.contains(constraint,true) == true ||
                        element.standardNameI2?.contains(constraint,true) == true ||
                        element.standardNameI3?.contains(constraint,true) == true}
//                   // TODO: filter it
//                    result.forEach {
//                        it.standardNameB1 = null
//                        it.standardLevelFaultsB1 = null
//                        it.standardNameB3 = null
//                        it.standardLevelFaultsB3 = null
//                        it.standardNameB2 = null
//                        it.standardLevelFaultsB2 = null
//                    }
                    if (result.isNotEmpty()) {
                        filterResults.values = result
                        filterResults.count = result.size
                        return filterResults
                    }


                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results?.count!! > 0) {
                    filterResult = results?.values as List<ControlList>
                    viewModel.emptyResults.value = false
                    notifyDataSetChanged()
                } else {
                   viewModel.emptyResults.value = true
                }

            }

        }
        return filter
    }


}