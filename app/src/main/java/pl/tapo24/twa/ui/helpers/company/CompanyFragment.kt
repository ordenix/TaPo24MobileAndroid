package pl.tapo24.twa.ui.helpers.company

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
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.CompanyAdapter
import pl.tapo24.twa.databinding.FragmentCompanyBinding

@AndroidEntryPoint
class CompanyFragment : Fragment() {

    private var _binding: FragmentCompanyBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = CompanyFragment()
    }

    private lateinit var viewModel: CompanyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompanyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(CompanyViewModel::class.java)


        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = CompanyAdapter(viewModel.data.value.orEmpty(), viewModel.data.value.orEmpty(), viewModel)
        rv.adapter = viewModel.adapter
//
        viewModel.data.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.filterResults = it
            viewModel.adapter.notifyDataSetChanged()
        })


        binding.queryText.addTextChangedListener {
            viewModel.adapter.filter.filter(binding.queryText.text)
            viewModel.searchText = binding.queryText.text.toString()
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