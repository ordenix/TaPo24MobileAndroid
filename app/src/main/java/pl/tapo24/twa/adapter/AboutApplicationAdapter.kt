package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.dbData.entity.DataBaseVersion
import java.util.*

class AboutApplicationAdapter(
    var items: List<DataBaseVersion>
): RecyclerView.Adapter<AboutApplicationAdapter.AboutApplicationAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutApplicationAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_about_application_base, parent,false)
        return AboutApplicationAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: AboutApplicationAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class AboutApplicationAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataBaseVersion) {
            binding.setVariable(BR.data, item)
            binding.executePendingBindings()


        }
    }


}