package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.data.customCategory.CategoryToMap
import pl.tapo24.twa.ui.tariff.TariffViewModel
import java.util.*

class CustomCategoryMapAdapter(
    var items: List<CategoryToMap>,
    var viewModel: TariffViewModel
): RecyclerView.Adapter<CustomCategoryMapAdapter.CustomCategoryMapAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomCategoryMapAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_dialog_custom_category_map, parent,false)
        return CustomCategoryMapAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CustomCategoryMapAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class CustomCategoryMapAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategoryToMap) {
            binding.setVariable(BR.data, item)
            binding.setVariable(BR.viewModel, viewModel)

        }
    }


}