package pl.tapo24.twa.ui.road

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import pl.tapo24.twa.R
import pl.tapo24.twa.databinding.FragmentRoadBinding

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
            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_category_driving_licence
            )
        }

        binding.countryDrivingLicence.setOnClickListener { view ->
            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_country_driving_licence
            )
        }

        binding.status.setOnClickListener { view ->
            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_status
            )
        }
        binding.towing.setOnClickListener { view ->
            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_towing
            )
        }
        binding.lights.setOnClickListener { view ->
            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_lights
            )
        }
        binding.alcohol.setOnClickListener { view ->
            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_alcohol
            )
        }
        binding.sign.setOnClickListener { view ->
            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_sign
            )
        }

        binding.accident.setOnClickListener { view ->
            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_story,
                bundleOf("name" to "Kwalifikacja zdarzenia","storyType" to "accident")
            )
        }

        binding.uto.setOnClickListener { view ->
            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_uto
            )
        }

        binding.CodeLimitsDrivingLicence.setOnClickListener { view ->
            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_codeLimitsDrivingLicence
            )
        }

        binding.controlList.setOnClickListener { view ->
            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_controlList
            )
        }
        binding.recidivism.setOnClickListener { view ->
            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_recidivism
            )
        }
        binding.codePoints.setOnClickListener { view ->
            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_codePoints
            )
        }
        binding.holdingDocuments.setOnClickListener { view ->
            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
            view.findNavController().navigate(
                R.id.action_nav_road_to_nav_holdingDocumentsChose
            )
        }
//        binding.ll.performClick()
//        binding.ll.setOnTouchListener(object: SwipeListener(context) {
//
//            override fun onSwipeRight() {
//                // https://stackoverflow.com/questions/47107105/android-button-has-setontouchlistener-called-on-it-but-does-not-override-perform
//                binding.root.findNavController().navigate(
//                    R.id.action_nav_road_to_nav_tariff
//                )
//                super.onSwipeRight()
//            }
//
//            override fun onSwipeLeft() {
//                super.onSwipeLeft()
//                binding.root.findNavController().navigate(
//                    R.id.action_nav_road_to_nav_home
//                )
//            }
//
//        })
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