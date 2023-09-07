package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.dbData.entity.LightsOthers


class LightsOthersAdapter(
    var items: List<LightsOthers>
):RecyclerView.Adapter<LightsOthersAdapter.LightsOthersDataHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LightsOthersDataHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_lights_others,parent, false)
        return LightsOthersDataHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: LightsOthersDataHolder, position: Int) {
        val item = items[position]
        val size = items.size
        holder.bind(item, position,size)
    }
    inner class LightsOthersDataHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LightsOthers, position: Int, size: Int) {
            binding.setVariable(BR.data, item)
            binding.setVariable(BR.size, size)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()
        }
    }
}
