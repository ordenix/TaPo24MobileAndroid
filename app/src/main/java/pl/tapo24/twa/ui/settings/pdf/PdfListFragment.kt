package pl.tapo24.twa.ui.settings.pdf

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import pl.tapo24.twa.R
import pl.tapo24.twa.adapter.PdfListAdapter
import pl.tapo24.twa.databinding.FragmentPdfListBinding

@AndroidEntryPoint
class PdfListFragment : Fragment() {

    companion object {
        fun newInstance() = PdfListFragment()
    }


    private var _binding: FragmentPdfListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PdfListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPdfListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(PdfListViewModel::class.java)


        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(activity)
        viewModel.adapter = PdfListAdapter(viewModel.data.value.orEmpty(), viewModel)
        rv.adapter = viewModel.adapter
//
        viewModel.data.observe(viewLifecycleOwner, Observer {
            viewModel.adapter.items = it
            viewModel.adapter.notifyDataSetChanged()
        })

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}