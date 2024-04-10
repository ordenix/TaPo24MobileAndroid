package pl.tapo24.twa.ui.admin.survey.list

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.SurveyListAdapter
import pl.tapo24.twa.databinding.FragmentSignListBinding
import pl.tapo24.twa.databinding.FragmentSurveyListBinding

@AndroidEntryPoint
class SurveyListFragment : Fragment() {

    companion object {
        fun newInstance() = SurveyListFragment()
    }

    private var _binding: FragmentSurveyListBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel: SurveyListViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SurveyListViewModel::class.java)
        _binding = FragmentSurveyListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.getSurveyList()
        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = SurveyListAdapter(viewModel.surveyList.value.orEmpty(), viewModel)
        rv.adapter = viewModel.adapter
//
        viewModel.surveyList.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.filterResults = it
            viewModel.adapter.notifyDataSetChanged()
        })
        binding.queryText.addTextChangedListener {
            viewModel.adapter.filter.filter(binding.queryText.text)
        }
        binding.rv.setOnTouchListener(View.OnTouchListener { v, event ->
            val inputManager: InputMethodManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(requireView().windowToken, 0)
            false
        })
        binding.button3.setOnClickListener {
            findNavController().navigate(R.id.action_surveyListFragment_to_surveyModifyFragment)
        }
        viewModel.selectedSurvey.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.publish) {
                    findNavController().navigate(R.id.action_surveyListFragment_to_surveyProgressFragment,
                        bundleOf("survey" to it)
                    )
                } else {
                    findNavController().navigate(R.id.action_surveyListFragment_to_surveyModifyFragment,
                        bundleOf("survey" to it)
                    )
                }

                viewModel.selectedSurvey.value = null

            }

        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}