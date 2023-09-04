package pl.tapo24.ui.road.holdingDocuments.type

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.tapo24.R
import pl.tapo24.databinding.FragmentHoldingDocumentsBinding
import pl.tapo24.databinding.FragmentLawBinding

class HoldingDocumentsFragment : Fragment() {

    private var _binding: FragmentHoldingDocumentsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: HoldingDocumentsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(HoldingDocumentsViewModel::class.java)
        _binding = FragmentHoldingDocumentsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}