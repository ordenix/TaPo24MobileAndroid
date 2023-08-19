package pl.tapo24.ui.road.towing

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
import pl.tapo24.adapter.TowingAdapter
import pl.tapo24.databinding.FragmentSettingBinding
import pl.tapo24.databinding.FragmentTowingBinding

@AndroidEntryPoint
class TowingFragment : Fragment() {
    private var _binding: FragmentTowingBinding? = null
    private val binding get() = _binding!!




    private lateinit var viewModel: TowingViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(TowingViewModel::class.java)
        _binding = FragmentTowingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.getData()
        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = TowingAdapter(viewModel.data.value.orEmpty(), viewModel.data.value.orEmpty())
        rv.adapter = viewModel.adapter
        binding.queryText.addTextChangedListener {
            viewModel.adapter.filter.filter(binding.queryText.text)
        }

        viewModel.data.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.filterResult = it

            viewModel.adapter.notifyDataSetChanged()
        })
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