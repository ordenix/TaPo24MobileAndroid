package pl.tapo24.twa.ui.road.codePoints

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
import pl.tapo24.twa.adapter.CodePointsAdapter
import pl.tapo24.twa.databinding.FragmentCodePointsBinding

@AndroidEntryPoint
class CodePointsFragment : Fragment() {

    private var _binding: FragmentCodePointsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: CodePointsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(CodePointsViewModel::class.java)
        _binding = FragmentCodePointsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getData()
        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = CodePointsAdapter(viewModel.data.value.orEmpty(),viewModel.data.value.orEmpty(), viewModel)
        rv.adapter = viewModel.adapter
//
        viewModel.data.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it.orEmpty()
            viewModel.adapter.filterResult = it.orEmpty()
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

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}