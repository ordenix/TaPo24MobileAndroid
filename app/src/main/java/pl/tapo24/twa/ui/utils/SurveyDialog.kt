package pl.tapo24.twa.ui.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.SurveyResponseAdapter
import pl.tapo24.twa.data.SelectableSearchableDialogElement
import pl.tapo24.twa.data.survey.Answers
import pl.tapo24.twa.data.survey.ResponseSurvey
import pl.tapo24.twa.data.survey.Survey
import pl.tapo24.twa.data.survey.SurveyAnswer

class SurveyDialog(var survey: Survey
    ): DialogFragment() {
    var onSendSurveyClick: ((ResponseSurvey) -> Unit)? = null
    var onNonSelectedAnswerSurveyClick: (() -> Unit)? = null
    var onRejectSurveyClick: (() -> Unit)? = null

    override fun show(manager: FragmentManager, tag: String?) {
        if (!this.isAdded) {
            super.show(manager, tag)
        }
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        if (!this.isAdded) {
            return super.show(transaction, tag)
        }
        return 0

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(pl.tapo24.twa.R.layout.dialog_survey, container, false)
        val tileView = view.findViewById<TextView>(R.id.titleSurvey)
        val type = view.findViewById<TextView>(R.id.typeSurvey)
        if (survey.singleChoice) {
            type.text = "Jednokrotny wybór"
        } else {
            type.text = "Wielokrotny wybór"
        }
        val rv = view.findViewById<RecyclerView>(R.id.rv)
        val rejectButton = view.findViewById<Button>(R.id.rejectSurvey)
        val okButton = view.findViewById<Button>(R.id.sendSurvey)
        tileView.text = survey.name
        rejectButton.setOnClickListener {
            onRejectSurveyClick?.invoke()
        }
        val sortedList = survey.surveyAnswers?.toList()?.sortedBy { it.sortId }
        val adapter = SurveyResponseAdapter(sortedList ?: emptyList())
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = adapter
        adapter.onItemClick = {
            if (survey.singleChoice) {
                var selectedAnswer = 0
                survey.surveyAnswers?.forEach { element->
                    if (element.isSelected) {
                        selectedAnswer += 1
                    }
                }
                if (selectedAnswer>=1) {
                    adapter.selectedEnabled = false
                } else {
                    adapter.selectedEnabled = true
                }
                adapter.notifyDataSetChanged()

            }

        }
        okButton.setOnClickListener {
            if (sortedList?.find { it.isSelected } != null) {
                val responseSurvey = ResponseSurvey(sortedList.filter { el -> el.isSelected }.map { Answers(it.id!!) }, survey.id ?: 0)
                onSendSurveyClick?.invoke(responseSurvey)
            } else {
                onNonSelectedAnswerSurveyClick?.invoke()
            }
        }
        return view
    }


    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onPause() {
        this.dismiss()
        super.onPause()
    }

}