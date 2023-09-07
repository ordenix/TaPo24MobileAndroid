package pl.tapo24.twa.ui.road.uto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentUtoBinding

class UtoFragment : Fragment() {

    private var _binding: FragmentUtoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: UtoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUtoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.uto1.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_utoFragment_to_nav_utoComponent,
                bundleOf("id" to 1)
            )
        }
        binding.uto2.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_utoFragment_to_nav_utoComponent,
                bundleOf("id" to 2)
            )
        }
        binding.uto3.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_utoFragment_to_nav_utoComponent,
                bundleOf("id" to 3)
            )
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}