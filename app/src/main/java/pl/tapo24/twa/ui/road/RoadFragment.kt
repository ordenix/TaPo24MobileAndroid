package pl.tapo24.twa.ui.road

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import pl.tapo24.twa.adapter.NavAdapter
import pl.tapo24.twa.data.nav.NavRoad
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
//        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
//            binding.grid.columnCount = 4
//        } else {
//            binding.grid.columnCount = 3
//        }


        val rv = binding.rv
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            rv.layoutManager = GridLayoutManager(activity,4)
        } else {

            rv.layoutManager = GridLayoutManager(activity,3)
        }

        roadViewModel.adapter = NavAdapter(NavRoad.values().map {it.navElement})

        rv.adapter = roadViewModel.adapter

        roadViewModel.adapter.onActiveItemClick = { navElement ->
            if (navElement.navId != null) {
                requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
                view?.findNavController()?.navigate(
                    navElement.navId,
                    navElement?.listBundle
                )
            } else if (navElement.navHttpLink != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(navElement.navHttpLink))
                startActivity(intent)
            }
        }
        roadViewModel.adapter.onDeActiveItemClick = { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
        }
//
//        viewModel.data.observe(viewLifecycleOwner, Observer {
//            viewModel.adapter.items = it
//            viewModel.adapter.notifyDataSetChanged()
//        })

//        binding.CategoryDrivingLicence.setOnClickListener {view->
//            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
//            view.findNavController().navigate(
//                R.id.action_nav_road_to_nav_category_driving_licence
//            )
//        }
//
//        binding.countryDrivingLicence.setOnClickListener { view ->
//            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
//            view.findNavController().navigate(
//                R.id.action_nav_road_to_nav_country_driving_licence
//            )
//        }
//
//        binding.status.setOnClickListener { view ->
//            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
//            view.findNavController().navigate(
//                R.id.action_nav_road_to_nav_status
//            )
//        }
//        binding.towing.setOnClickListener { view ->
//            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
//            view.findNavController().navigate(
//                R.id.action_nav_road_to_nav_towing
//            )
//        }
//        binding.lights.setOnClickListener { view ->
//            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
//            view.findNavController().navigate(
//                R.id.action_nav_road_to_nav_lights
//            )
//        }
//        binding.alcohol.setOnClickListener { view ->
//            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
//            view.findNavController().navigate(
//                R.id.action_nav_road_to_nav_alcohol
//            )
//        }
//        binding.sign.setOnClickListener { view ->
//            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
//            view.findNavController().navigate(
//                R.id.action_nav_road_to_nav_sign
//            )
//        }
//
//        binding.accident.setOnClickListener { view ->
//            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
//            view.findNavController().navigate(
//                R.id.action_nav_road_to_nav_story,
 //          bundleOf("name" to "Kwalifikacja zdarzenia","storyType" to "accident")
//            )
//        }
//
//        binding.uto.setOnClickListener { view ->
//            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
//            view.findNavController().navigate(
//                R.id.action_nav_road_to_nav_uto
//            )
//        }
//
//        binding.CodeLimitsDrivingLicence.setOnClickListener { view ->
//            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
//            view.findNavController().navigate(
//                R.id.action_nav_road_to_nav_codeLimitsDrivingLicence
//            )
//        }
//
//        binding.controlList.setOnClickListener { view ->
//            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
//            view.findNavController().navigate(
//                R.id.action_nav_road_to_nav_controlList
//            )
//        }
//        binding.recidivism.setOnClickListener { view ->
//            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
//            view.findNavController().navigate(
//                R.id.action_nav_road_to_nav_recidivism
//            )
//        }
//        binding.codePoints.setOnClickListener { view ->
//            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
//            view.findNavController().navigate(
//                R.id.action_nav_road_to_nav_codePoints
//            )
//        }
//        binding.holdingDocuments.setOnClickListener { view ->
//            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
//            view.findNavController().navigate(
//                R.id.action_nav_road_to_nav_holdingDocumentsChose
//            )
//        }
//        binding.checkList.setOnClickListener { view ->
//            requireActivity().findViewById<com.google.android.material.appbar.AppBarLayout>(pl.tapo24.twa.R.id.AppBarLayout)?.setExpanded(true)
//            if (State.premiumVersion) {
//                view.findNavController().navigate(
//                    R.id.action_nav_road_to_nav_checkListList
//                )
//            } else {
//                Snackbar.make(view, getString(R.string.premiumNotify), Snackbar.LENGTH_LONG).show()
//            }
//
//        }
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