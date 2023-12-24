package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.db.entity.CustomCategory
import java.util.*

class CustomCategoryAdapter(
    var items: List<CustomCategory>
): RecyclerView.Adapter<CustomCategoryAdapter.CustomCategoryAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomCategoryAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_custom_category, parent,false)
        return CustomCategoryAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CustomCategoryAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class CustomCategoryAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CustomCategory) {
            binding.setVariable(BR.data, item)


        }
    }


}