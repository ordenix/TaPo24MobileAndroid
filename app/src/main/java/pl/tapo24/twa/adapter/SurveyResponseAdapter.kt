package pl.tapo24.twa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.data.survey.Answers
import pl.tapo24.twa.data.survey.SurveyAnswer
import java.util.*

class SurveyResponseAdapter(
    var items: List<SurveyAnswer>,
    var selectedEnabled: Boolean = true
): RecyclerView.Adapter<SurveyResponseAdapter.SurveyResponseAdapterHolder>() {
    var onItemClick: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyResponseAdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_element_response_survey_answer, parent, false)
        return SurveyResponseAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SurveyResponseAdapterHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    inner class SurveyResponseAdapterHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bind(item: SurveyAnswer) {
            val checkBox = view.findViewById<CheckBox>(R.id.checkBox2)
            val container = view.findViewById<View>(R.id.container)
            checkBox.text = item.answer
            checkBox.isChecked = item.isSelected
            //checkBox.isEnabled = false
            container.setOnClickListener {
                if (!selectedEnabled) {
                    if (item.isSelected) {
                        item.isSelected = false
                        checkBox.isChecked = item.isSelected
                        //onItemClick?.invoke()
                    } else {
                        items.forEach {
                            it.isSelected = false
                        }
                        item.isSelected = true
                        onItemClick?.invoke()
                    }
                } else {
                    item.isSelected = !item.isSelected
                    checkBox.isChecked = item.isSelected
                    onItemClick?.invoke()
                }
            }
            checkBox.setOnClickListener {
                if (!selectedEnabled) {
                    if (item.isSelected) {
                        item.isSelected = false
                        checkBox.isChecked = item.isSelected
                        //onItemClick?.invoke()
                    } else {
                        items.forEach {
                            it.isSelected = false
                        }
                        item.isSelected = true
                        onItemClick?.invoke()
                    }
                } else {
                    item.isSelected = !item.isSelected
                    checkBox.isChecked = item.isSelected
                    onItemClick?.invoke()
                }
            }
//            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
//                if (item.isSelected != isChecked) {
//                    if (selectedEnabled) {
//                        // multichoice
//                        item.isSelected = isChecked
//                    } else {
//                        //singlechoice
//                        if (item.isSelected) {
//                            //items.forEach { it.isSelected = false }
//                            item.isSelected = false
//                        } else {
//                            items.forEach { it.isSelected = false }
//                            item.isSelected = true
//                        }
//                    }
//                    onItemClick?.invoke()
//                }
//
//            }


//            if (!selectedEnabled && item.isSelected) {
//                checkBox.isEnabled = true
//            } else {
//                checkBox.isEnabled = selectedEnabled
//            }
//            checkBox.setOnCheckedChangeListener{ _, isChecked ->
//                if (item.isSelected != isChecked) {
//                    item.isSelected = isChecked
//                    onItemClick?.invoke()
//                }
//
//            }
//            container.setOnClickListener {
//                if (selectedEnabled || item.isSelected) {
//                    item.isSelected = !item.isSelected
//                    checkBox.isChecked = item.isSelected
//                    onItemClick?.invoke()
//                }
//
//            }


        }
    }


}