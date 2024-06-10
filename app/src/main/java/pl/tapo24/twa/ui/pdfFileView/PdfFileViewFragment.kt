package pl.tapo24.twa.ui.pdfFileView

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.PdfAdapter
import pl.tapo24.twa.databinding.FragmentPdfFileViewBinding
import pl.tapo24.twa.utils.PdfOpenIntent
import java.io.File


@AndroidEntryPoint

class PdfFileViewFragment: Fragment() {
    private var _binding: FragmentPdfFileViewBinding? = null
    private val binding get() = _binding!!
    var title = "Przepisy"
    lateinit var viewModel: LawViewModel


    override fun onResume() {
        requireActivity().findViewById<Toolbar>(R.id.toolbar)?.title = title
        //requireActivity().findViewById<Toolbar>(R.id.toolbar)?
        val args = arguments?.let { PdfFileViewFragmentArgs.fromBundle(it) }
        viewModel.getData(args!!.type)
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val args = arguments?.let { PdfFileViewFragmentArgs.fromBundle(it) }
        title = args!!.title
        viewModel = ViewModelProvider(this).get(LawViewModel::class.java)

        super.onCreate(savedInstanceState)
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val args = arguments?.let { PdfFileViewFragmentArgs.fromBundle(it) }
        _binding = FragmentPdfFileViewBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.getData(args!!.type)
        if (args.type == "law") {
            binding.pdfList.visibility = View.VISIBLE

        }
        binding.pdfList.setOnClickListener {
            findNavController().navigate(R.id.action_nav_law_to_pdfListFragment)
        }
        val rv = binding.rv
        rv.layoutManager = GridLayoutManager(activity,3)
        viewModel.adapter = PdfAdapter(viewModel.data.value.orEmpty(), viewModel.data.value.orEmpty())
        rv.adapter = viewModel.adapter
//
        viewModel.data.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.filterResult = it
            viewModel.adapter.notifyDataSetChanged()
        })
        viewModel.adapter.onItemClick = {
            it.fileName?.let { it1 -> PdfOpenIntent(requireContext()).openPDF(it1, viewModel.isPublicStorage) }
        }

        // TODO: refactor abofe becaus dup?

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