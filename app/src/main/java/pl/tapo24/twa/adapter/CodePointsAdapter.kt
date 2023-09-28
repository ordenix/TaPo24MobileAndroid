package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.dbData.entity.CodePointsNew
import pl.tapo24.twa.ui.road.codePoints.CodePointsViewModel
import java.util.*

class CodePointsAdapter(
    var items: List<CodePointsNew>,
    var viewModel: CodePointsViewModel
): RecyclerView.Adapter<CodePointsAdapter.CodePointsAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CodePointsAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_code_points, parent,false)
        return CodePointsAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CodePointsAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem, position)
    }

    inner class CodePointsAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CodePointsNew, position: Int) {
            binding.setVariable(BR.data,item)
            binding.setVariable(BR.position,position)
            binding.setVariable(BR.viewModel,viewModel)
            binding.executePendingBindings()



        }
    }


}