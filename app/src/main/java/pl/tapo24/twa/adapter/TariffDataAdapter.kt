package pl.tapo24.twa.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.BR
import pl.tapo24.twa.R
import pl.tapo24.twa.db.entity.Tariff
import pl.tapo24.twa.ui.tariff.TariffViewModel
import java.io.File


class TariffDataAdapter(
    var items: List<Tariff>,
    var viewModel: TariffViewModel?

): RecyclerView.Adapter<TariffDataAdapter.TariffDataHolder>() {
    var onItemClick: ((Tariff) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TariffDataHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_tariff, parent, false)
        return TariffDataHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TariffDataHolder, position: Int) {
        val currentItem: Tariff = items[position]
        holder.bind(currentItem, position)
    }
    inner class TariffDataHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {
//        val textView: TextView = view.findViewById(R.id.textView2)
    private var onItemClickListener: ((View) -> Unit)? = null
        fun bind (item: Tariff, position: Int) {
            binding.root.findViewById<ConstraintLayout>(R.id.ss).setOnClickListener(onItemClickListener)
            binding.setVariable(BR.data, item)
            binding.setVariable(BR.viewModel,viewModel)
            binding.setVariable(BR.position, position)
            if (item.path != null) {
                val file: File = File(binding.root.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), item.path!!)
                if (file.exists()) {
                    val bitmap: Bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    binding.setVariable(BR.image, bitmap)
                }
            }else {
                binding.setVariable(BR.image, null)
            }

            binding.executePendingBindings()
//            btn.setOnClickListener {
//                onItemClick?.invoke(item)
//            }
//            val str = SpannableString("Highlighted. Not highlighted.")
//            str.setSpan(BackgroundColorSpan(Color.YELLOW), 0, 11, 0)
//            textView.setText(str)

        }

    }


}