package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.data.SpbChose
import pl.tapo24.twa.dbData.entity.Sign
import java.util.*

class SpbChoseAdapter(

    var items: List<SpbChose>
): RecyclerView.Adapter<SpbChoseAdapter.SpbChoseAdapterHolder>() {
    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpbChoseAdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_element_spb_chose, parent, false)
        return SpbChoseAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SpbChoseAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class SpbChoseAdapterHolder(val view: View): RecyclerView.ViewHolder(view) {
        private val image1: ImageView = view.findViewById<ImageView>(R.id.image1)
        private val image2: ImageView = view.findViewById<ImageView>(R.id.image2)
        private val label: TextView = view.findViewById<TextView>(R.id.textLabelSpbChose)
        private val container: ConstraintLayout = view.findViewById(R.id.container)



        fun bind(item: SpbChose) {
            container.setOnClickListener {
                onItemClick?.invoke(item.type)
            }
            label.text = item.type
            if (item.path2 == null) {
                image2.visibility = View.GONE
            } else {
                val ico2 = view.context.resources.getIdentifier(item.path2,"drawable",view.context.packageName)
                image2.setImageResource(ico2)
            }
            val ico1 = view.context.resources.getIdentifier(item.path1,"drawable",view.context.packageName)
            image1.setImageResource(ico1)


        }
    }


}