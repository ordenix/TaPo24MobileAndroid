package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.dbData.entity.Law
import pl.tapo24.twa.ui.settings.pdf.PdfListViewModel
import java.util.*

class PdfListAdapter(
    var items: List<Law>,
    var viewModel: PdfListViewModel
): RecyclerView.Adapter<PdfListAdapter.PdfListAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfListAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_pdf, parent,false)
        return PdfListAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PdfListAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class PdfListAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Law) {
            val checkBox = binding.root.findViewById<CheckBox>(R.id.checkBox)
            checkBox.isChecked = item.isSelectedToDownload == true
            if (item.isOptional == false) {
                binding.setVariable(BR.fileStatus, "Zawartość głównej paczki")
                checkBox.isChecked = true
            } else if (item.isSelectedToDownload == true) {
                binding.setVariable(BR.fileStatus, "Plik pobrany lokalnie")
            } else {
                binding.setVariable(BR.fileStatus, "Plik dostępny do pobrania")
            }
            binding.setVariable(BR.viewModel, viewModel)
            binding.setVariable(BR.data, item)



        }
    }


}