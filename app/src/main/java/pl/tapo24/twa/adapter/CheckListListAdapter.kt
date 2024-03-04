package pl.tapo24.twa.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.db.entity.CheckListType
import pl.tapo24.twa.ui.road.checkList.CheckListListViewModel
import java.util.*

class CheckListListAdapter(
    var items: List<CheckListType>,
    var viewModel: CheckListListViewModel
): RecyclerView.Adapter<CheckListListAdapter.CheckListListAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckListListAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_check_list_type, parent,false)
        return CheckListListAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CheckListListAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class CheckListListAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {
        private val pdfIcon: ImageView = binding.root.findViewById<ImageView>(R.id.imageViewA)
        fun bind(item: CheckListType) {

            val image = binding.root.context.resources.getIdentifier(item.iconName,"drawable",binding.root.context.packageName)
            pdfIcon.setImageResource(image)


            binding.setVariable(BR.data, item)
            binding.setVariable(BR.viewModel, viewModel)
            binding.executePendingBindings()

        }
    }


}