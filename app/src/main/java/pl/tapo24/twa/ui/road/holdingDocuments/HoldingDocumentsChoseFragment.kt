package pl.tapo24.twa.ui.road.holdingDocuments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentHoldingDocumentsChoseBinding

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

        binding.holdingDocumentsProofOfRegistration.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_nav_holdingDocumentsChose_to_holdingDocumentsFragment,
                bundleOf("title" to "ZDR","type" to "proof_of_registration", "otherCountry" to false)
            )
        }
        binding.holdingDocumentsProofOfRegistrationOther.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_nav_holdingDocumentsChose_to_holdingDocumentsFragment,
                bundleOf("title" to "ZDR zagraniczne","type" to "proof_of_registration", "otherCountry" to true)
            )
        }
        binding.holdingDrivingLicence.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_nav_holdingDocumentsChose_to_holdingDocumentsFragment,
                bundleOf("title" to "ZPJ","type" to "driving_licence", "otherCountry" to false)
            )
        }
        binding.holdingDrivingLicenceOther.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_nav_holdingDocumentsChose_to_holdingDocumentsFragment,
                bundleOf("title" to "ZPJ zagraniczne","type" to "driving_licence", "otherCountry" to true)
            )
        }
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}