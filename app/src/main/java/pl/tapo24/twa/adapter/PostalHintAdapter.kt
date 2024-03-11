package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import java.util.*

class PostalHintAdapter(
    var items: List<String>
): RecyclerView.Adapter<PostalHintAdapter.PostalHintAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostalHintAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_postal_hint, parent,false)
        return PostalHintAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PostalHintAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class PostalHintAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.setVariable(BR.name, item)
            binding.executePendingBindings()


        }
    }


}