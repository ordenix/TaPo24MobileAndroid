package pl.tapo24.adapter

import android.graphics.Color
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.badge.BadgeUtils
import pl.tapo24.R
import pl.tapo24.db.entity.Tariff


class TariffDataAdapter(
    var items: List<Tariff>

): RecyclerView.Adapter<TariffDataAdapter.TariffDataHolder>() {
    var onItemClick: ((Tariff) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TariffDataHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tariff_rv_element, parent, false)
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