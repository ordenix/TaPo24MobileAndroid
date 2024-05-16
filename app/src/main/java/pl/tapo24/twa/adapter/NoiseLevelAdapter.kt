package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.dbData.entity.NoiseLevel
import java.util.*

class NoiseLevelAdapter(
    var items: List<NoiseLevel>
): RecyclerView.Adapter<NoiseLevelAdapter.NoiseLevelAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoiseLevelAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_noise_level, parent,false)
        return NoiseLevelAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NoiseLevelAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class NoiseLevelAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NoiseLevel) {
            binding.setVariable(BR.data, item)
            binding.executePendingBindings()


        }
    }


}