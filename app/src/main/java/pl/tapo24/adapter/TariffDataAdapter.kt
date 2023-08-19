package pl.tapo24.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.R
import pl.tapo24.db.entity.Tariff


class TariffDataAdapter(
    var items: List<Tariff>

): RecyclerView.Adapter<TariffDataAdapter.TariffDataHolder>() {
    var onItemClick: ((Tariff) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TariffDataHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_element_tariff, parent, false)
        return TariffDataHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TariffDataHolder, position: Int) {
        val currentItem:Tariff = items[position]
        holder.bind(currentItem)
    }
    inner class TariffDataHolder(val view: View): RecyclerView.ViewHolder(view) {
//        val textView: TextView = view.findViewById(R.id.textView2)
        val btn: Button = view.findViewById(R.id.button4)
        fun bind (item: Tariff) {
            btn.setOnClickListener {
                onItemClick?.invoke(item)
            }
//            val str = SpannableString("Highlighted. Not highlighted.")
//            str.setSpan(BackgroundColorSpan(Color.YELLOW), 0, 11, 0)
//            textView.setText(str)

        }

    }


}