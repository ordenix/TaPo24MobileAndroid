package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.data.checkListMap.CheckListComplex
import pl.tapo24.twa.ui.road.checkList.checkList.CheckListViewModel
import java.util.*

class CheckListAdapter(
    var items: List<CheckListComplex>,
    var viewModel: CheckListViewModel
): RecyclerView.Adapter<CheckListAdapter.CheckListAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckListAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_check_list, parent,false)
        return CheckListAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CheckListAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem, position)
    }

    inner class CheckListAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CheckListComplex, position: Int) {
            binding.setVariable(BR.data, item)
            binding.setVariable(BR.viewModel, viewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()


        }
    }


}