package pl.tapo24.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.R
import pl.tapo24.BR
import pl.tapo24.db.entity.LastSearch
import java.util.*

class QuerySuggestionAdapter(
    var items: List<LastSearch>
): RecyclerView.Adapter<QuerySuggestionAdapter.QuerySuggestionAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuerySuggestionAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_query_suggestion, parent,false)
        return QuerySuggestionAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: QuerySuggestionAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class QuerySuggestionAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LastSearch) {
            binding.setVariable(BR.data, item)
            binding.executePendingBindings()


        }
    }


}