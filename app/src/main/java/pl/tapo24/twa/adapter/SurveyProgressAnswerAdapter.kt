package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.data.survey.ProgressAnswer
import java.util.*

class SurveyProgressAnswerAdapter(
    var items: List<ProgressAnswer>
): RecyclerView.Adapter<SurveyProgressAnswerAdapter.SurveyProgressAnswerAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyProgressAnswerAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_survey_answer_progress, parent,false)
        return SurveyProgressAnswerAdapterHolder(binding)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SurveyProgressAnswerAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class SurveyProgressAnswerAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item:ProgressAnswer) {
            binding.setVariable(BR.data, item)
            binding.executePendingBindings()


        }
    }


}