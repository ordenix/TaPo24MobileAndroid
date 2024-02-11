package pl.tapo24.twa.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.data.SelectableSearchableDialogElement
import pl.tapo24.twa.db.entity.Tariff
import java.util.*

class SelectableSearchableDialogAdapter(
    var items: List<SelectableSearchableDialogElement>,
    val tileField1: String,
    val tileField2: String?,
    val tileField3: String?,
    val isMultiSelect: Boolean
): RecyclerView.Adapter<SelectableSearchableDialogAdapter.SelectableSearchableDialogAdapterHolder>(), Filterable {
    private var filterResults: List<SelectableSearchableDialogElement> = items
    var onItemClick: ((SelectableSearchableDialogElement) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableSearchableDialogAdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_element_dialog_selected_searchable, parent, false)
        return SelectableSearchableDialogAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        return filterResults.size
    }

    override fun onBindViewHolder(holder: SelectableSearchableDialogAdapterHolder, position: Int) {
        val currentItem = filterResults[position]
        holder.bind(currentItem)
    }

//override fun getItemId(position: Int): Long {
//        return items[position].hashCode().toLong()
//    }
    inner class SelectableSearchableDialogAdapterHolder(val view: View): RecyclerView.ViewHolder(view) {
        private val tile1 = view.findViewById<TextView>(R.id.titleText1)
        private val tile2 = view.findViewById<TextView>(R.id.titleText2)
        private val tile3 = view.findViewById<TextView>(R.id.titleText3)
        private val field1 = view.findViewById<TextView>(R.id.fieldText1)
        private val field2 = view.findViewById<TextView>(R.id.fieldText2)
        private val field3 = view.findViewById<TextView>(R.id.fieldText3)
        private val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
        fun bind(item: SelectableSearchableDialogElement) {
            if (tileField2 == null) {
                tile2.visibility = View.GONE
                field2.visibility = View.GONE
            }
            if (tileField3 == null) {
                tile3.visibility = View.GONE
                field3.visibility = View.GONE
            }
            tile1.text = tileField1
            tile2.text = tileField2
            tile3.text = tileField3
            field1.text = item.f1
            field2.text = item.f2
            field3.text = item.f3
            checkBox.isChecked = item.isSelected
            //checkBox.is
            if (isMultiSelect) {
                checkBox.visibility = View.VISIBLE
                view.setOnClickListener {
                    item.isSelected = !item.isSelected
                    notifyItemChanged(adapterPosition)
                }
                checkBox.setOnClickListener {
                    item.isSelected = !item.isSelected
                    notifyItemChanged(adapterPosition)
                }

            } else {
                checkBox.visibility = View.GONE
                view.setOnClickListener {
                    onItemClick?.invoke(item)
                }
            }


        }
    }

    override fun getFilter(): Filter {

        val filter = object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (p0.isNullOrEmpty()) {
                    filterResults.count = items.size
                    filterResults.values = items
                } else {
                    val result = items.filter { element -> element.f1.contains(p0, true)
                            || element.f2.contains(p0, true)
                            || element.f3.contains(p0, true) }
                    filterResults.count = result.size
                    filterResults.values = result
                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filterResults = p1?.values as List<SelectableSearchableDialogElement>
                notifyDataSetChanged()
            }
        }
        return filter
    }


}