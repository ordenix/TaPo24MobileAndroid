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
import pl.tapo24.twa.data.survey.Survey
import pl.tapo24.twa.ui.admin.survey.list.SurveyListViewModel
import java.util.*

class SurveyListAdapter(
    var items: List<Survey>,
    val viewModel: SurveyListViewModel
): RecyclerView.Adapter<SurveyListAdapter.SurveyListAdapterHolder>(), Filterable {
     var filterResults: List<Survey> = items


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyListAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_survey_list, parent,false)
        return SurveyListAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return filterResults.size
    }

    override fun onBindViewHolder(holder: SurveyListAdapterHolder, position: Int) {
        val currentItem = filterResults[position]
        holder.bind(currentItem)
    }

    inner class SurveyListAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Survey) {
            binding.setVariable(BR.data, item)
            val currentTime = System.currentTimeMillis()/1000
            if (currentTime > item.endDate) {
                binding.setVariable(BR.finish, true)
            } else {
                binding.setVariable(BR.finish, false)
            }
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
                        val result = items.filter {
                            element -> element.name!!.contains(constraint.toString(), ignoreCase = true)
                        }
                        filterResults.values = result
                        filterResults.count = result.size
                    }
                    return filterResults
                }

                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    filterResults = results?.values as List<Survey>
                    notifyDataSetChanged()

                }
            }
            return filter
        }


}