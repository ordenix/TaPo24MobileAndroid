package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.dbData.entity.Status
import pl.tapo24.twa.ui.road.status.StatusViewModel

class StatusAdapter(
    var viewModelStatusViewModel: StatusViewModel,
    var items: List<Status>
):RecyclerView.Adapter<StatusAdapter.StatusDataHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusDataHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_status, parent, false)
        return StatusDataHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: StatusDataHolder, position: Int) {
        val item = items[position]
        holder.bind(item, position)
    }

    inner class StatusDataHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Status, position: Int) {
            binding.setVariable(BR.position, position)
            binding.setVariable(BR.data,item)
            binding.setVariable(BR.viewModel, viewModelStatusViewModel)
            binding.executePendingBindings()
        }
    }
}