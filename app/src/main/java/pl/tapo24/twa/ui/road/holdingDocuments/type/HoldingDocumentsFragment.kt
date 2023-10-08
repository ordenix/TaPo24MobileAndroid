package pl.tapo24.twa.ui.road.holdingDocuments.type

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.HoldingDocumentsAdapter
import pl.tapo24.twa.databinding.FragmentHoldingDocumentsBinding
import pl.tapo24.twa.ui.story.StoryFragmentArgs


@AndroidEntryPoint
class HoldingDocumentsFragment : Fragment() {

    private var _binding: FragmentHoldingDocumentsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var title: String = ""
    var otherCountry: Boolean = false
    var type: String = ""

    private lateinit var viewModel: HoldingDocumentsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        val args = arguments?.let { HoldingDocumentsFragmentArgs.fromBundle(it) }
        title = args!!.title
        otherCountry = args.otherCountry
        type = args.type

        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(HoldingDocumentsViewModel::class.java)
        _binding = FragmentHoldingDocumentsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.getData(type, otherCountry)

        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = HoldingDocumentsAdapter(viewModel.data.value.orEmpty(), viewModel.data.value.orEmpty())
        rv.adapter = viewModel.adapter
//
        viewModel.data.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.filterResult = it
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
    override fun onResume() {
        requireActivity().findViewById<Toolbar>(R.id.toolbar)?.title = title
        super.onResume()
    }

}