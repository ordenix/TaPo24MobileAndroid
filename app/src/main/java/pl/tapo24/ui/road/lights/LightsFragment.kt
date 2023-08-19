package pl.tapo24.ui.road.lights

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import pl.tapo24.R
import pl.tapo24.databinding.FragmentLightsBinding
import pl.tapo24.databinding.FragmentLightsCodeCountryBinding

class LightsFragment : Fragment() {
    private var _binding: FragmentLightsBinding? = null
    private val binding get() = _binding!!



    private lateinit var viewModel: LightsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LightsViewModel::class.java)

        _binding = FragmentLightsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lightsCodeCountry.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_nav_lights_to_nav_lights_code_country
            )
        }

        binding.lightsFront.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_nav_lights_to_nav_lights_front
            )
        }

        binding.lightsOthers.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_nav_lights_to_nav_lights_others
            )
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}