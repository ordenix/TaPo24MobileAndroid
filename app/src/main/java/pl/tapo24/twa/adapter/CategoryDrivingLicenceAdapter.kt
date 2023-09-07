package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.dbData.entity.CodeDrivingLicence
import pl.tapo24.twa.ui.road.categoryDrivingLicence.CategoryDrivingLicenceViewModel

class CategoryDrivingLicenceAdapter(
    var items: List<CodeDrivingLicence>,
    var viewModel: CategoryDrivingLicenceViewModel
): RecyclerView.Adapter<CategoryDrivingLicenceAdapter.CategoryDrivingLicenceDataHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryDrivingLicenceDataHolder {
        val view = LayoutInflater.from(parent.context)
        // var binding = DataBindingUtil.inflate<ViewDataBinding>(view, layout, parent, false)
       val binding =  DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_category_driving_licence, parent, false)
        return CategoryDrivingLicenceDataHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CategoryDrivingLicenceDataHolder, position: Int) {
        val item = items[position]
        holder.bind(item, item.expand, position)
    }

    inner class CategoryDrivingLicenceDataHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CodeDrivingLicence, isExpand: Boolean, position: Int){
            binding.setVariable(BR.data, item)
            binding.setVariable(BR.viewModel, viewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()

        }

    }
}