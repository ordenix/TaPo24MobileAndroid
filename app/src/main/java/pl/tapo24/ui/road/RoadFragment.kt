package pl.tapo24.ui.road

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import pl.tapo24.R
import pl.tapo24.databinding.FragmentRoadBinding
import pl.tapo24.ui.tariff.TariffViewModel

class RoadFragment: Fragment() {
    private var _binding: FragmentRoadBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val currentOrientation = resources.configuration.orientation
        val roadViewModel =
            ViewModelProvider(this).get(RoadViewModel::class.java)

        _binding = FragmentRoadBinding.inflate(inflater, container, false)
        val root: View = binding.root
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.grid.columnCount = 4
        } else {
            binding.grid.columnCount = 3
        }

        binding.CategoryDrivingLicence.setOnClickListener {view->
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_category_driving_licence
            )
        }

        binding.countryDrivingLicence.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_country_driving_licence
            )
        }

        binding.status.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_status
            )
        }
        binding.towing.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_towing
            )
        }
        binding.lights.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_lights
            )
        }
        binding.alcohol.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_alcohol
            )
        }
        binding.sign.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_sign
            )
        }

        binding.accident.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_story,
                bundleOf("name" to "Kwalifikacja zdarzenia","storyType" to "accident")
            )
        }

        binding.uto.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_uto
            )
        }

        binding.controlList.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_controlList
            )
        }

//        val textView: TextView = binding.textViewRoad
//        roadViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}