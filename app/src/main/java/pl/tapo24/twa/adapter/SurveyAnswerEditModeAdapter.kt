package pl.tapo24.twa.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.BR
import pl.tapo24.twa.data.survey.SurveyAnswer
import pl.tapo24.twa.ui.admin.survey.modify.SurveyModifyViewModel

class SurveyAnswerEditModeAdapter(
    var items: List<SurveyAnswer>,
    var viewModel: SurveyModifyViewModel
): RecyclerView.Adapter<SurveyAnswerEditModeAdapter.SurveyAnswerModifyAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyAnswerModifyAdapterHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(view, R.layout.rv_element_survey_answer, parent,false)
        return SurveyAnswerModifyAdapterHolder(binding)



    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SurveyAnswerModifyAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class SurveyAnswerModifyAdapterHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SurveyAnswer) {
            binding.setVariable(BR.data, item)
            binding.setVariable(BR.viewModel, viewModel)


        }
    }


}