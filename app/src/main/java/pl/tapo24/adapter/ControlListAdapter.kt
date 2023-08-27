package pl.tapo24.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.R
import pl.tapo24.BR
import pl.tapo24.dbData.entity.ControlList
import pl.tapo24.ui.road.controlList.ControlListViewModel
import java.util.*

class ControlListAdapter(
    var items: List<ControlList>,
    var viewModel: ControlListViewModel
): RecyclerView.Adapter<ControlListAdapter.ControlListAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ControlListAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_control_list, parent,false)
        return ControlListAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ControlListAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class ControlListAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ControlList) {
            binding.setVariable(BR.data, item)
            binding.setVariable(BR.viewModel, viewModel)
            binding.executePendingBindings()

        }
    }


}