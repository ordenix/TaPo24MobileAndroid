package pl.tapo24.twa.ui.admin.survey.progress

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.SurveyProgressAnswerAdapter
import pl.tapo24.twa.data.survey.Survey
import pl.tapo24.twa.databinding.FragmentSurveyProgressBinding
import pl.tapo24.twa.ui.admin.survey.modify.SurveyModifyFragmentArgs
import pl.tapo24.twa.utils.DataFormatterConverter

@AndroidEntryPoint
class SurveyProgressFragment : Fragment(), DataFormatterConverter {

    companion object {
        fun newInstance() = SurveyProgressFragment()
    }

    private var _binding: FragmentSurveyProgressBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel: SurveyProgressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SurveyProgressViewModel::class.java)
        _binding = FragmentSurveyProgressBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val args = arguments?.let { SurveyProgressFragmentArgs.fromBundle(it) }
        if (args!!.survey != null) {
            viewModel.survey = args.survey
            viewModel.getSurveyProgress()
        }
        binding.viewModel = viewModel
        binding.dateEndTIET.setText(convertTimeStampToHumanCalendarDate(viewModel.survey.endDate.toInt()))
        binding.dateStartTIET.setText(convertTimeStampToHumanCalendarDate(viewModel.survey.startDate.toInt()))
        if (viewModel.survey.toLoginUser == null && viewModel.survey.toVipUser == null) {
            binding.target.text = getString(R.string.targetToAll)
        }
        // only login with vip
        if (viewModel.survey.toLoginUser == true && viewModel.survey.toVipUser == true) {
            binding.target.text = getString(R.string.targetAllLogin)
        }
        // non login
        if (viewModel.survey.toLoginUser == false && viewModel.survey.toVipUser == null) {
            binding.target.text = getString(R.string.targetOnlyNonLogin)
        }
        // only vip
        if (viewModel.survey.toLoginUser == false && viewModel.survey.toVipUser == true) {
            binding.target.text = getString(R.string.targetOnlyVip)
        }
        // only login non vip
        if (viewModel.survey.toLoginUser == true && viewModel.survey.toVipUser == false) {
            binding.target.text = getString(R.string.targetOnlyLoginNonVip)
        }
        binding.status.text = returnStatus(viewModel.survey)
        viewModel.progress.observe(viewLifecycleOwner) {
            println(it)
        }

        val rv = binding.recyclerView
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = SurveyProgressAnswerAdapter(viewModel.progress.value?.answerList  ?: emptyList())
        rv.adapter = viewModel.adapter
//
        viewModel.progress.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it.answerList
            binding.totalUser.text = "Odpowiedziało: ${it.totalUserResponse} użytkowników"
            binding.userRejected.text = "Odrzucono: ${it.totalUserRejectResponse} ankiet"
            viewModel.adapter.notifyDataSetChanged()
        })
        viewModel.moveBack.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().popBackStack()
                viewModel.moveBack.value = false
            }
        })
        binding.changeStatus.setOnClickListener {
            viewModel.changeSurveyStatus()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun returnStatus(survey: Survey): String {
        val currentTime = System.currentTimeMillis() / 1000
        return when {
            survey.startDate > currentTime -> {
                if (survey.active) {
                   return "Ankieta nie rozpoczęta. Dostępna dla użytkowników"
                } else {
                    return "Ankieta nie rozpoczęta. Nie dostępna dla użytkowników"

                }
            }
            survey.endDate < currentTime -> {
                "Ankieta zakończona"
            }
            else -> {
                if (survey.active) {
                    return "Ankieta w trakcie. Dostępna dla użytkowników"
                } else {
                    return "Ankieta w trakcie. Nie dostępna dla użytkowników"
                }
            }
        }

    }

}