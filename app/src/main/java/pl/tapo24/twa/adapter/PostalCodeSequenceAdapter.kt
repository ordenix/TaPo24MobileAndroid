package pl.tapo24.twa.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.data.postal.ResponseCodeSequenceContent
import pl.tapo24.twa.utils.UlListBuilder
import java.util.*

class PostalCodeSequenceAdapter(
    var items: List<ResponseCodeSequenceContent>
): RecyclerView.Adapter<PostalCodeSequenceAdapter.PostalCodeSequenceAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostalCodeSequenceAdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_element_postal_code_sequence, parent, false)
        return PostalCodeSequenceAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PostalCodeSequenceAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class PostalCodeSequenceAdapterHolder(val view: View): RecyclerView.ViewHolder(view) {
        private val rvTvMsc: TextView = view.findViewById<TextView>(R.id.rv_tv_msc)
        private val rvTvGm = view.findViewById<TextView>(R.id.rv_tv_gm)
        private val rvTvPow = view.findViewById<TextView>(R.id.rv_tv_pow)
        private val rvTvWoj = view.findViewById<TextView>(R.id.rv_tv_woj)
        private val rvLeft = view.findViewById<TextView>(R.id.rv_left)
        private val rvRight = view.findViewById<TextView>(R.id.rv_right)
        fun bind(item: ResponseCodeSequenceContent) {
            rvTvMsc.text = item.miejscowosc
            rvTvGm.text = item.gmina
            rvTvPow.text = item.powiat
            rvTvWoj.text = item.wojewodztwo
            val leftList:MutableList<String> = mutableListOf()
            val rightList:MutableList<String> = mutableListOf()

            item.codeSequence.forEachIndexed { index, s ->
                if (index % 2 == 0) {
                    leftList.add(s)
                } else {
                    rightList.add(s)
                }

            }
            rvRight.text = UlListBuilder().getSpannableTextBullet(rightList, true)
            if (rightList.isEmpty()) {
                rvRight.visibility = View.GONE
            }
            rvLeft.text = UlListBuilder().getSpannableTextBullet(leftList, true)


        }
    }


}