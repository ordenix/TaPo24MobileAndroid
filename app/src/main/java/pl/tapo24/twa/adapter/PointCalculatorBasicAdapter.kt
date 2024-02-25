package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.dbData.entity.CodePointsNew
import pl.tapo24.twa.ui.helpers.pointsCalc.basic.PointsCalculatorBasicViewModel
import java.util.*

class PointCalculatorBasicAdapter(
    var items: List<CodePointsNew>,
    var viewModel: PointsCalculatorBasicViewModel
): RecyclerView.Adapter<PointCalculatorBasicAdapter.PointCalculatorBasicAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointCalculatorBasicAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_points_calculator_baisic, parent,false)
        return PointCalculatorBasicAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PointCalculatorBasicAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem, position)
    }

    inner class PointCalculatorBasicAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CodePointsNew, position: Int) {
            binding.setVariable(BR.data, item)
            binding.setVariable(BR.viewModel, viewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()


        }
    }


}