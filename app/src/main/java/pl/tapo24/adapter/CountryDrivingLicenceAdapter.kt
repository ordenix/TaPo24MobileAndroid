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

import pl.tapo24.dbData.entity.CountryDrivingLicence
import pl.tapo24.ui.road.countryDrivingLicence.CountryDrivingLicenceViewModel

class CountryDrivingLicenceAdapter(
    var items: List<CountryDrivingLicence>,
    var filterResult: List<CountryDrivingLicence>,
    var viewModel: CountryDrivingLicenceViewModel
): RecyclerView.Adapter<CountryDrivingLicenceAdapter.CountryDrivingLicenceDataHolder>(), Filterable {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryDrivingLicenceDataHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_country_driving_licence, parent,false)
        return CountryDrivingLicenceDataHolder(binding)
    }

    override fun getItemCount(): Int {
        return filterResult.size
    }

    override fun onBindViewHolder(holder: CountryDrivingLicenceDataHolder, position: Int) {
        val item = filterResult[position]
        holder.bind(item)

    }

    inner class CountryDrivingLicenceDataHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CountryDrivingLicence) {
            binding.setVariable(BR.data,item)
            binding.setVariable(BR.viewModel,viewModel)
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
                    val result = items.filter { element -> element.country.contains(constraint,true) }
                    filterResults.count = result.size
                    filterResults.values = result
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterResult = results?.values as List<CountryDrivingLicence>
                notifyDataSetChanged()

            }

        }
        return filter
    }
    }
