package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.dbData.entity.CodeColors
import pl.tapo24.twa.ui.road.codeColors.viewer.CodeColorsViewerViewModel
import java.util.*

class CodeColorsAdapter(
    var items: List<CodeColors>,
    var viewModel: CodeColorsViewerViewModel
): RecyclerView.Adapter<CodeColorsAdapter.CodeColorsAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CodeColorsAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_code_colors, parent,false)
        return CodeColorsAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CodeColorsAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class CodeColorsAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CodeColors) {
            binding.setVariable(BR.data, item)
            binding.setVariable(BR.viewModel, viewModel)
            binding.executePendingBindings()

        }
    }


}