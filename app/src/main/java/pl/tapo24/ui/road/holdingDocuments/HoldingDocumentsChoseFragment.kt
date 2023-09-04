package pl.tapo24.ui.road.holdingDocuments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.tapo24.R
import pl.tapo24.databinding.FragmentHoldingDocumentsChoseBinding
import pl.tapo24.databinding.FragmentLawBinding
import pl.tapo24.ui.road.holdingDocuments.type.HoldingDocumentsViewModel

class HoldingDocumentsChoseFragment : Fragment() {
    private var _binding: FragmentHoldingDocumentsChoseBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: HoldingDocumentsChoseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(HoldingDocumentsChoseViewModel::class.java)
        _binding = FragmentHoldingDocumentsChoseBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}