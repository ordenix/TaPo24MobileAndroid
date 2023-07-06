package pl.tapo24.adapter

import android.icu.text.SimpleDateFormat
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.R
import pl.tapo24.data.State
import pl.tapo24.db.entity.WhatsNews
import java.util.*

class HomeWhatsNewsAdapter(
    var items: List<WhatsNews>
): RecyclerView.Adapter<HomeWhatsNewsAdapter.HomeWhatsNewsHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeWhatsNewsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.whats_news_rv_element, parent, false)
        return HomeWhatsNewsHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: HomeWhatsNewsHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class HomeWhatsNewsHolder(val view: View): RecyclerView.ViewHolder(view) {
        private val date: TextView = view.findViewById(R.id.textViewDate)
        private val topic: TextView = view.findViewById(R.id.textViewTitle)
        private val p1: TextView = view.findViewById(R.id.textViewP1)
        private val p2: TextView = view.findViewById(R.id.textViewP2)
        private val p3: TextView = view.findViewById(R.id.textViewP3)
        private val p4: TextView = view.findViewById(R.id.textViewP4)
        private val footerBold: TextView = view.findViewById(R.id.textViewFooterBold)
        private val footer: TextView = view.findViewById(R.id.textViewFooter)
        private val autor: TextView = view.findViewById(R.id.textView2Author)
        private val version: TextView = view.findViewById(R.id.textViewVersion)
        fun bind(item: WhatsNews) {
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val newDate = Date((item.date * 1000L).toLong())
                date.text = sdf.format(newDate)
            } catch (e: Exception) {
                e.toString()
            }


            topic.text = item.title
            p1.text = item.p1
            p2.text = item.p2
            p3.text = item.p3
            p4.text = item.p4
            footerBold.text = item.footerBold
            footer.text = item.footer
            autor.text = item.author
            version.text = State.versionName
            if (item.p1.isEmpty()) {
                p1.visibility = GONE
            }
            if (item.p2.isEmpty()) {
                p2.visibility = GONE
            }
//            if (item.p3.isEmpty()) {
//                p3.visibility = GONE
//            }
            if (item.p4.isEmpty()) {
                p4.visibility = GONE
            }

            if (item.footerBold.isEmpty()) {
                footerBold.visibility = GONE
            }
            if (item.footer.isEmpty()) {
                footer.visibility = GONE
            }
            p3.text = "1xxxxxxxxx\n2xxx\n3xxxx\n4xxxx\n5xxxxxxxxx\n6xxxxxxxxxxxxxxx\n1\n1\n1\n1\n1\n"
           // p3.movementMethod= ScrollingMovementMethod()


        }
    }


}