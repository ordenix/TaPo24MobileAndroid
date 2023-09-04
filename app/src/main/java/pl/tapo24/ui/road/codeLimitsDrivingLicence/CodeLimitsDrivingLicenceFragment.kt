package pl.tapo24.ui.road.codeLimitsDrivingLicence

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.R
import pl.tapo24.adapter.CodeLimitsDrivingLicenceAdapter
import pl.tapo24.databinding.FragmentCodeLimitsDrivingLicenceBinding
import pl.tapo24.databinding.FragmentLawBinding
@AndroidEntryPoint

class CodeLimitsDrivingLicenceFragment : Fragment() {

    private var _binding: FragmentCodeLimitsDrivingLicenceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: CodeLimitsDrivingLicenceViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(CodeLimitsDrivingLicenceViewModel::class.java)
        _binding = FragmentCodeLimitsDrivingLicenceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getData()
        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = CodeLimitsDrivingLicenceAdapter(viewModel.data.value.orEmpty(), viewModel.data.value.orEmpty() , viewModel)
        rv.adapter = viewModel.adapter
//
        viewModel.data.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.filterResult = it
            viewModel.adapter.notifyDataSetChanged()
                })

        binding.queryText.addTextChangedListener {
            viewModel.searchText = binding.queryText.text.toString()
            viewModel.adapter.filter.filter(binding.queryText.text)
        }
        binding.rv.setOnTouchListener(View.OnTouchListener { v, event ->
            val inputManager: InputMethodManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(requireView().windowToken, 0)
            false
        })
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}